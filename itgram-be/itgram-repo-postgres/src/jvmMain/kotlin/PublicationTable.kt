package ru.itgram.backend.repo.postgresql

import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import ru.itgram.common.models.MkplPublication
import ru.itgram.common.models.MkplPublicationId
import ru.itgram.common.models.MkplPublicationLock
import ru.itgram.common.models.MkplUserId

class PublicationTable(tableName: String) : Table(tableName) {
    val id = text(SqlFields.ID)
    val title = text(SqlFields.TITLE).nullable()
    val description = text(SqlFields.DESCRIPTION).nullable()
    val owner = text(SqlFields.OWNER_ID)
    val visibility = visibilityEnumeration(SqlFields.VISIBILITY)
    val publicationCategory = publicationCategoryEnumeration((SqlFields.PUBLICATION_CATEGORY))
    val lock = text(SqlFields.LOCK)

    override val primaryKey = PrimaryKey(id)

    fun from(res: ResultRow): MkplPublication {
        return MkplPublication(
            id = MkplPublicationId(res[id].toString()),
            title = res[title] ?: "",
            description = res[description] ?: "",
            ownerId = MkplUserId(res[owner].toString()),
            visibility = res[visibility],
            publicationCategory = res[publicationCategory],
            lock = MkplPublicationLock(res[lock]),
        )
    }

    fun to(it: UpdateBuilder<*>, publication: MkplPublication, randomUuid: () -> String) {
        it[id] = publication.id.takeIf { it != MkplPublicationId.NONE }?.asString() ?: randomUuid()
        it[title] = publication.title
        it[description] = publication.description
        it[owner] = publication.ownerId.asString()
        it[visibility] = publication.visibility
        it[publicationCategory] = publication.publicationCategory
        it[lock] = publication.lock.takeIf { it != MkplPublicationLock.NONE }?.asString() ?: randomUuid()
    }

}

