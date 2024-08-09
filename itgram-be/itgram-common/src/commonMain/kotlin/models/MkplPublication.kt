package ru.itgram.common.models

data class MkplPublication(
    var id: MkplPublicationId = MkplPublicationId.NONE,
    var title: String = "",
    var description: String = "",
    var ownerId: MkplUserId = MkplUserId.NONE,
    var publicationCategory: MkplPublicationCategory = MkplPublicationCategory.POST,
    var visibility: MkplVisibility = MkplVisibility.NONE,
    var lock: MkplPublicationLock = MkplPublicationLock.NONE,
    val permissionsClient: MutableSet<MkplPublicationPermissionClient> = mutableSetOf()
) {
    fun isEmpty() = this == NONE

    companion object {
        private val NONE = MkplPublication()
    }

}
