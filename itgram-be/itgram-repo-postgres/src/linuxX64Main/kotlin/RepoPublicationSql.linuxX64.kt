package ru.itgram.backend.repo.postgresql

import io.github.moreirasantos.pgkn.PostgresDriver
import io.github.moreirasantos.pgkn.resultset.ResultSet
import kotlinx.coroutines.runBlocking
import ru.itgram.backend.repo.postgresql.SqlFields.quoted
import ru.itgram.common.models.MkplPublication
import ru.itgram.common.models.MkplPublicationId
import ru.itgram.common.models.MkplPublicationLock
import ru.itgram.common.models.MkplUserId
import ru.itgram.common.repo.*
import ru.itgram.repo.common.IRepoPublicationInitializable

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class RepoPublicationSql actual constructor(
    properties: SqlProperties,
    val randomUuid: () -> String,
) : IRepoPublication, IRepoPublicationInitializable {
    init {
        require(properties.database.matches(Regex("^[\\w\\d_]+\$"))) {
            "PostgreSQL database must contain only letters, numbers and underscore symbol '_'"
        }
        require(properties.schema.matches(Regex("^[\\w\\d_]+\$"))) {
            "PostgreSQL schema must contain only letters, numbers and underscore symbol '_'"
        }
        require(properties.table.matches(Regex("^[\\w\\d_]+\$"))) {
            "PostgreSQL table must contain only letters, numbers and underscore symbol '_'"
        }
    }

    private val dbName: String = "\"${properties.schema}\".\"${properties.table}\"".apply {
        // Валидируем, что админ не ошибся в имени таблицы
    }
    init {
        initConnection(properties)
    }

    private suspend fun saveElement(savePublication: MkplPublication): IDbPublicationResponse {
        val sql = """
                INSERT INTO $dbName (
                  ${SqlFields.ID.quoted()}, 
                  ${SqlFields.TITLE.quoted()}, 
                  ${SqlFields.DESCRIPTION.quoted()},
                  ${SqlFields.VISIBILITY.quoted()},
                  ${SqlFields.PUBLICATION_CATEGORY.quoted()},
                  ${SqlFields.LOCK.quoted()},
                  ${SqlFields.OWNER_ID.quoted()},
                  ${SqlFields.PRODUCT_ID.quoted()}
                ) VALUES (
                  :${SqlFields.ID}, 
                  :${SqlFields.TITLE}, 
                  :${SqlFields.DESCRIPTION}, 
                  :${SqlFields.VISIBILITY}::${SqlFields.VISIBILITY_TYPE}, 
                  :${SqlFields.PUBLICATION_CATEGORY}::${SqlFields.PUBLICATION_CATEGORY_TYPE}, 
                  :${SqlFields.LOCK}, 
                  :${SqlFields.OWNER_ID}, 
                  :${SqlFields.PRODUCT_ID}
                )
                RETURNING ${SqlFields.allFields.joinToString()}
            """.trimIndent()
        val res = driver.execute(
            sql = sql,
            savePublication.toDb(),
        ) { row: ResultSet -> row.fromDb(SqlFields.allFields) }
        return DbPublicationResponseOk(res.first())
    }

    actual override fun save(publications: Collection<MkplPublication>): Collection<MkplPublication> = runBlocking {
        publications.map {
            val res = saveElement(it)
            if (res !is DbPublicationResponseOk) throw Exception()
            res.data
        }
    }

    actual override suspend fun createPublication(rq: DbPublicationRequest): IDbPublicationResponse {
        val saveAd = rq.publication.copy(id = MkplPublicationId(randomUuid()), lock = MkplPublicationLock(randomUuid()))
        return saveElement(saveAd)
    }

    actual override suspend fun readPublication(rq: DbPublicationIdRequest): IDbPublicationResponse {
        val sql = """
                SELECT ${SqlFields.allFields.joinToString { it.quoted() }}
                FROM $dbName
                WHERE ${SqlFields.ID.quoted()} = :${SqlFields.ID}
            """.trimIndent()
        val res = driver.execute(
            sql = sql,
            rq.id.toDb(),
        ) { row: ResultSet -> row.fromDb(SqlFields.allFields) }
        return if (res.isEmpty()) errorNotFound(rq.id) else DbPublicationResponseOk(res.first())
    }

    actual override suspend fun updatePublication(rq: DbPublicationRequest): IDbPublicationResponse {
        val sql = """
            WITH update_obj AS (
                UPDATE $dbName a
                SET ${SqlFields.TITLE.quoted()} = :${SqlFields.TITLE}
                , ${SqlFields.DESCRIPTION.quoted()} = :${SqlFields.DESCRIPTION}
                , ${SqlFields.PUBLICATION_CATEGORY.quoted()} = :${SqlFields.PUBLICATION_CATEGORY}::${SqlFields.PUBLICATION_CATEGORY_TYPE}
                , ${SqlFields.VISIBILITY.quoted()} = :${SqlFields.VISIBILITY}::${SqlFields.VISIBILITY_TYPE}
                , ${SqlFields.LOCK.quoted()} = :${SqlFields.LOCK}
                , ${SqlFields.OWNER_ID.quoted()} = :${SqlFields.OWNER_ID}
                WHERE  a.${SqlFields.ID.quoted()} = :${SqlFields.ID}
                AND a.${SqlFields.LOCK.quoted()} = :${SqlFields.LOCK_OLD}
                RETURNING ${SqlFields.allFields.joinToString()}
            ),
            select_obj AS (
                SELECT ${SqlFields.allFields.joinToString()} FROM $dbName 
                WHERE ${SqlFields.ID.quoted()} = :${SqlFields.ID} 
            )
            (SELECT * FROM update_obj UNION ALL SELECT * FROM select_obj) LIMIT 1
        """.trimIndent()
        val rqPublication = rq.publication
        val newAd = rqPublication.copy(lock = MkplPublicationLock(randomUuid()))
        val res = driver.execute(
            sql = sql,
            newAd.toDb() + rqPublication.lock.toDb(),
        ) { row: ResultSet -> row.fromDb(SqlFields.allFields) }
        val returnedPublication: MkplPublication? = res.firstOrNull()
        return when {
            returnedPublication == null -> errorNotFound(rq.publication.id)
            returnedPublication.lock == newAd.lock -> DbPublicationResponseOk(returnedPublication)
            else -> errorRepoConcurrency(returnedPublication, rqPublication.lock)
        }
    }

    actual override suspend fun deletePublication(rq: DbPublicationIdRequest): IDbPublicationResponse {
        val sql = """
            WITH delete_obj AS (
                DELETE FROM $dbName a
                WHERE  a.${SqlFields.ID.quoted()} = :${SqlFields.ID}
                AND a.${SqlFields.LOCK.quoted()} = :${SqlFields.LOCK_OLD}
                RETURNING '${SqlFields.DELETE_OK}'
            )
            SELECT ${SqlFields.allFields.joinToString()}, (SELECT * FROM delete_obj) as flag FROM $dbName 
            WHERE ${SqlFields.ID.quoted()} = :${SqlFields.ID} 
        """.trimIndent()
        val res = driver.execute(
            sql = sql,
            rq.id.toDb() + rq.lock.toDb(),
        ) { row: ResultSet -> row.fromDb(SqlFields.allFields) to row.getString(SqlFields.allFields.size) }
        val returnedPair: Pair<MkplPublication,String?>? = res.firstOrNull()
        val returnedPublication: MkplPublication? = returnedPair?.first
        return when {
            returnedPublication == null -> errorNotFound(rq.id)
            returnedPair.second == SqlFields.DELETE_OK -> DbPublicationResponseOk(returnedPublication)
            else -> errorRepoConcurrency(returnedPublication, rq.lock)
        }
    }

    actual override suspend fun searchPublication(rq: DbPublicationFilterRequest): IDbPublicationsResponse {
        val where = listOfNotNull(
            rq.ownerId.takeIf { it != MkplUserId.NONE }
                ?.let { "${SqlFields.OWNER_ID.quoted()} = :${SqlFields.OWNER_ID}" },
            rq.publicationCategory
                ?.let { "${SqlFields.PUBLICATION_CATEGORY.quoted()} = :${SqlFields.PUBLICATION_CATEGORY}::${SqlFields.PUBLICATION_CATEGORY_TYPE}" },
            rq.titleFilter.takeIf { it.isNotBlank() }
                ?.let { "${SqlFields.TITLE.quoted()} LIKE :${SqlFields.TITLE}" },
        )
            .takeIf { it.isNotEmpty() }
            ?.let { "WHERE ${it.joinToString(separator = " AND ")}" }
            ?: ""

        val sql = """
                SELECT ${SqlFields.allFields.joinToString { it.quoted() }}
                FROM $dbName $where
            """.trimIndent()
        println("SQL: $sql")
        val res = driver.execute(
            sql = sql,
            rq.toDb(),
        ) { row: ResultSet -> row.fromDb(SqlFields.allFields) }
        return DbPublicationsResponseOk(res)
    }

    actual fun clear(): Unit = runBlocking {
        val sql = """
                DELETE FROM $dbName 
            """.trimIndent()
        driver.execute(sql = sql)
    }

    companion object {
        private lateinit var driver: PostgresDriver
        private fun initConnection(properties: SqlProperties) {
            if (!this::driver.isInitialized) {
                driver = PostgresDriver(
                    host = properties.host,
                    port = properties.port,
                    user = properties.user,
                    database = properties.database,
                    password = properties.password,
                )
            }
        }
    }
}
