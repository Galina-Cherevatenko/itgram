package ru.itgram

import ru.itgram.common.models.*

abstract class BaseInitPublications(private val op: String): IInitObjects<MkplPublication> {
    fun createInitTestModel(
        suf: String,
        ownerId: MkplUserId = MkplUserId("owner-123"),
        publicationCategory: MkplPublicationCategory = MkplPublicationCategory.POST,
    ) = MkplPublication(
        id = MkplPublicationId("publication-repo-$op-$suf"),
        title = "$suf stub",
        description = "$suf stub description",
        ownerId = ownerId,
        visibility = MkplVisibility.VISIBLE_TO_OWNER,
        publicationCategory = publicationCategory,
    )
}