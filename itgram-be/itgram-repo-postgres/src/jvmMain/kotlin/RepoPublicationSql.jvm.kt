package ru.itgram.backend.repo.postgresql

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import ru.itgram.common.helpers.asMkplError
import ru.itgram.common.models.MkplPublication
import ru.itgram.common.models.MkplPublicationId
import ru.itgram.common.models.MkplPublicationLock
import ru.itgram.common.models.MkplUserId
import ru.itgram.common.repo.*
import ru.itgram.repo.common.IRepoPublicationInitializable

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class RepoPublicationSql actual constructor(
    properties: SqlProperties,
    private val randomUuid: () -> String
) : IRepoPublication, IRepoPublicationInitializable {
    private val publicationTable = PublicationTable("${properties.schema}.${properties.table}")

    private val driver = when {
        properties.url.startsWith("jdbc:postgresql://") -> "org.postgresql.Driver"
        else -> throw IllegalArgumentException("Unknown driver for url ${properties.url}")
    }

    private val conn = Database.connect(
        properties.url, driver, properties.user, properties.password
    )

    actual fun clear(): Unit = transaction(conn) {
        publicationTable.deleteAll()
    }

    private fun saveObj(publication: MkplPublication): MkplPublication = transaction(conn) {
        val res = publicationTable
            .insert {
                to(it, publication, randomUuid)
            }
            .resultedValues
            ?.map { publicationTable.from(it) }
        res?.first() ?: throw RuntimeException("BD error: insert statement returned empty result")
    }

    private suspend inline fun <T> transactionWrapper(
        crossinline block: () -> T,
        crossinline handle: (Exception) -> T
    ): T =
        withContext(Dispatchers.IO) {
            try {
                transaction(conn) {
                    block()
                }
            } catch (e: Exception) {
                handle(e)
            }
        }

    private suspend inline fun transactionWrapper(crossinline block: () -> IDbPublicationResponse): IDbPublicationResponse =
        transactionWrapper(block) { DbPublicationResponseErr(it.asMkplError()) }

    actual override fun save(publications: Collection<MkplPublication>): Collection<MkplPublication> =
        publications.map { saveObj(it) }

    actual override suspend fun createPublication(rq: DbPublicationRequest): IDbPublicationResponse =
        transactionWrapper {
            DbPublicationResponseOk(saveObj(rq.publication))
        }

    private fun read(id: MkplPublicationId): IDbPublicationResponse {
        val res = publicationTable.selectAll().where {
            publicationTable.id eq id.asString()
        }.singleOrNull() ?: return errorNotFound(id)
        return DbPublicationResponseOk(publicationTable.from(res))
    }

    actual override suspend fun readPublication(rq: DbPublicationIdRequest): IDbPublicationResponse =
        transactionWrapper { read(rq.id) }

    private suspend fun update(
        id: MkplPublicationId,
        lock: MkplPublicationLock,
        block: (MkplPublication) -> IDbPublicationResponse
    ): IDbPublicationResponse =
        transactionWrapper {
            if (id == MkplPublicationId.NONE) return@transactionWrapper errorEmptyId

            val current = publicationTable.selectAll().where { publicationTable.id eq id.asString() }
                .singleOrNull()
                ?.let { publicationTable.from(it) }

            when {
                current == null -> errorNotFound(id)
                current.lock != lock -> errorRepoConcurrency(current, lock)
                else -> block(current)
            }
        }


    actual override suspend fun updatePublication(rq: DbPublicationRequest): IDbPublicationResponse =
        update(rq.publication.id, rq.publication.lock) {
            publicationTable.update({ publicationTable.id eq rq.publication.id.asString() }) {
                to(it, rq.publication.copy(lock = MkplPublicationLock(randomUuid())), randomUuid)
            }
            read(rq.publication.id)
        }

    actual override suspend fun deletePublication(rq: DbPublicationIdRequest): IDbPublicationResponse =
        update(rq.id, rq.lock) {
            publicationTable.deleteWhere { id eq rq.id.asString() }
            DbPublicationResponseOk(it)
        }

    actual override suspend fun searchPublication(rq: DbPublicationFilterRequest): IDbPublicationsResponse =
        transactionWrapper({
            val res = publicationTable.selectAll().where {
                buildList {
                    add(Op.TRUE)
                    if (rq.ownerId != MkplUserId.NONE) {
                        add(publicationTable.owner eq rq.ownerId.asString())
                    }
                    if (rq.publicationCategory != null) {
                        add(publicationTable.publicationCategory eq rq.publicationCategory!!)
                    }
                    if (rq.titleFilter.isNotBlank()) {
                        add(
                            (publicationTable.title like "%${rq.titleFilter}%")
                                    or (publicationTable.description like "%${rq.titleFilter}%")
                        )
                    }
                }.reduce { a, b -> a and b }
            }
            DbPublicationsResponseOk(data = res.map { publicationTable.from(it) })
        }, {
            DbPublicationsResponseErr(it.asMkplError())
        })
}