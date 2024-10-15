package ru.itgram.backend.repo.postgresql

import io.github.moreirasantos.pgkn.resultset.ResultSet
import ru.itgram.common.models.*

internal fun ResultSet.fromDb(cols: List<String>): MkplPublication {
    val fieldsMap = cols.mapIndexed { i: Int, field: String -> field to i }.toMap()
    fun col(field: String): String? = fieldsMap[field]?.let { getString(it) }
    return MkplPublication(
        id = col(SqlFields.ID)?.let { MkplPublicationId(it) } ?: MkplPublicationId.NONE,
        title = col(SqlFields.TITLE) ?: "",
        description = col(SqlFields.DESCRIPTION) ?: "",
        ownerId = col(SqlFields.OWNER_ID)?.let { MkplUserId(it) } ?: MkplUserId.NONE,
        publicationCategory = col(SqlFields.PUBLICATION_CATEGORY).asPublicationCategory(),
        visibility = col(SqlFields.VISIBILITY).asVisibility(),
        lock = col(SqlFields.LOCK)?.let { MkplPublicationLock(it) } ?: MkplPublicationLock.NONE,
    )
}

private fun String?.asPublicationCategory(): MkplPublicationCategory = when (this) {
    SqlFields.PUBLICATION_CATEGORY_START -> MkplPublicationCategory.START
    SqlFields.PUBLICATION_CATEGORY_AD -> MkplPublicationCategory.AD
    else -> MkplPublicationCategory.POST
}

private fun String?.asVisibility(): MkplVisibility = when (this) {
    SqlFields.VISIBILITY_OWNER -> MkplVisibility.VISIBLE_TO_OWNER
    SqlFields.VISIBILITY_GROUP -> MkplVisibility.VISIBLE_TO_GROUP
    SqlFields.VISIBILITY_PUBLIC -> MkplVisibility.VISIBLE_PUBLIC
    else -> MkplVisibility.NONE
}
