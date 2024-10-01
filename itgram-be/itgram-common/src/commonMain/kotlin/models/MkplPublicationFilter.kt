package ru.itgram.common.models

data class MkplPublicationFilter(
    var searchString: String = "",
    var ownerId: MkplUserId = MkplUserId.NONE,
    var publicationCategory: MkplPublicationCategory = MkplPublicationCategory.POST,
){
    fun deepCopy(): MkplPublicationFilter = copy()

    fun isEmpty() = this == NONE

    companion object {
        private val NONE = MkplPublicationFilter()
    }
}
