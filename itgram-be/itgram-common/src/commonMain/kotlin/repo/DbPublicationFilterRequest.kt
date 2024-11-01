package ru.itgram.common.repo

import ru.itgram.common.models.MkplPublicationCategory
import ru.itgram.common.models.MkplUserId

data class DbPublicationFilterRequest(
    val titleFilter: String = "",
    val ownerId: MkplUserId = MkplUserId.NONE,
    val publicationCategory: MkplPublicationCategory? = MkplPublicationCategory.POST,
)