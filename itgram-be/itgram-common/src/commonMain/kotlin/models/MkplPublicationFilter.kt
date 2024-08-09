package ru.itgram.common.models

data class MkplPublicationFilter(
    var searchString: String = "",
    var ownerId: MkplUserId = MkplUserId.NONE,
    var publicationCategory: MkplPublicationCategory = MkplPublicationCategory.POST,
)
