import ru.itgram.common.models.*

data class PublicationEntity(
    val id: String? = null,
    val title: String? = null,
    val description: String? = null,
    val ownerId: String? = null,
    val publicationCategory: String? = null,
    val visibility: String? = null,
    val lock: String? = null,
) {
    constructor(model: MkplPublication): this(
        id = model.id.asString().takeIf { it.isNotBlank() },
        title = model.title.takeIf { it.isNotBlank() },
        description = model.description.takeIf { it.isNotBlank() },
        ownerId = model.ownerId.asString().takeIf { it.isNotBlank() },
        publicationCategory = model.publicationCategory.name,
        visibility = model.visibility.takeIf { it != MkplVisibility.NONE }?.name,
        lock = model.lock.asString().takeIf { it.isNotBlank() }
    )

    fun toInternal() = MkplPublication(
        id = id?.let { MkplPublicationId(it) }?: MkplPublicationId.NONE,
        title = title?: "",
        description = description?: "",
        ownerId = ownerId?.let { MkplUserId(it) }?: MkplUserId.NONE,
        publicationCategory = publicationCategory?.let { MkplPublicationCategory.valueOf(it) }?: MkplPublicationCategory.POST,
        visibility = visibility?.let { MkplVisibility.valueOf(it) }?: MkplVisibility.NONE,
        lock = lock?.let { MkplPublicationLock(it) } ?: MkplPublicationLock.NONE,
    )
}