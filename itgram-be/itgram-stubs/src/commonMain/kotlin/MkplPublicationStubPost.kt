import ru.itgram.common.models.*

object MkplPublicationStubPost {
    val PUBLICATION_POST_1: MkplPublication
        get() = MkplPublication(
            id = MkplPublicationId("123"),
            title = "Выступление на Jpoint",
            description = "Я выступил на конференции с докладом. Был полный зал. Это успех!",
            ownerId = MkplUserId("user-1"),
            publicationCategory = MkplPublicationCategory.POST,
            visibility = MkplVisibility.VISIBLE_PUBLIC,
            lock = MkplPublicationLock("123"),
            permissionsClient = mutableSetOf(
                MkplPublicationPermissionClient.READ,
                MkplPublicationPermissionClient.UPDATE,
                MkplPublicationPermissionClient.DELETE,
                MkplPublicationPermissionClient.MAKE_VISIBLE_PUBLIC,
                MkplPublicationPermissionClient.MAKE_VISIBLE_GROUP,
                MkplPublicationPermissionClient.MAKE_VISIBLE_OWNER,
            )
        )
    val PUBLICATION_START_1 = PUBLICATION_POST_1.copy(publicationCategory = MkplPublicationCategory.START)
}
