package ru.itgram.backend.repo.postgresql

import ru.itgram.common.models.*
import ru.itgram.common.repo.DbPublicationFilterRequest

private fun String.toDb() = this.takeIf { it.isNotBlank() }

internal fun MkplPublicationId.toDb() = mapOf(
    SqlFields.ID to asString().toDb(),
)
internal fun MkplPublicationLock.toDb() = mapOf(
    SqlFields.LOCK_OLD to asString().toDb(),
)

internal fun MkplPublication.toDb() = id.toDb() + mapOf(
    SqlFields.TITLE to title.toDb(),
    SqlFields.DESCRIPTION to description.toDb(),
    SqlFields.OWNER_ID to ownerId.asString().toDb(),
    SqlFields.PUBLICATION_CATEGORY to publicationCategory.toDb(),
    SqlFields.VISIBILITY to visibility.toDb(),
    SqlFields.LOCK to lock.asString().toDb(),
)

internal fun DbPublicationFilterRequest.toDb() = mapOf(
    // Используется для LIKE '%titleFilter%
    SqlFields.FILTER_TITLE to titleFilter.toDb()?.let { "%$it%"},
    SqlFields.FILTER_PUBLICATION_CATEGORY to publicationCategory?.toDb(),
    SqlFields.FILTER_OWNER_ID to ownerId.asString().toDb(),
)

private fun MkplPublicationCategory.toDb() = when (this) {
    MkplPublicationCategory.START -> SqlFields.PUBLICATION_CATEGORY_START
    MkplPublicationCategory.AD -> SqlFields.PUBLICATION_CATEGORY_AD
    else -> SqlFields.PUBLICATION_CATEGORY_POST
}

private fun MkplVisibility.toDb() = when (this) {
    MkplVisibility.VISIBLE_TO_OWNER -> SqlFields.VISIBILITY_OWNER
    MkplVisibility.VISIBLE_TO_GROUP -> SqlFields.VISIBILITY_GROUP
    MkplVisibility.VISIBLE_PUBLIC -> SqlFields.VISIBILITY_PUBLIC
    MkplVisibility.NONE -> null
}
