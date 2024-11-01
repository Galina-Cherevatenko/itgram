package ru.itgram

import ru.itgram.common.models.*

abstract class BaseInitPublications(private val op: String): IInitObjects<MkplPublication> {
    open val lockOld: MkplPublicationLock = MkplPublicationLock("20000000-0000-0000-0000-000000000001")
    open val lockBad: MkplPublicationLock = MkplPublicationLock("20000000-0000-0000-0000-000000000009")
    fun createInitTestModel(
        suf: String,
        ownerId: MkplUserId = MkplUserId("owner-123"),
        publicationCategory: MkplPublicationCategory = MkplPublicationCategory.START,
        lock: MkplPublicationLock = lockOld
    ) = MkplPublication(
        id = MkplPublicationId("publication-repo-$op-$suf"),
        title = "$suf stub",
        description = "$suf stub description",
        ownerId = ownerId,
        visibility = MkplVisibility.VISIBLE_TO_OWNER,
        publicationCategory = publicationCategory,
        lock = lock
    )
}