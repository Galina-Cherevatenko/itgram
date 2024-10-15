package src.main.kotlin

import ru.itgram.api.v1.models.PublicationCreateObject
import ru.itgram.api.v1.models.PublicationDeleteObject
import ru.itgram.api.v1.models.PublicationReadObject
import ru.itgram.api.v1.models.PublicationUpdateObject
import ru.itgram.common.models.MkplPublication
import ru.itgram.common.models.MkplPublicationId
import ru.itgram.common.models.MkplPublicationLock

fun MkplPublication.toTransportCreate() = PublicationCreateObject(
    title = title.takeIf { it.isNotBlank() },
    description = description.takeIf { it.isNotBlank() },
    publicationCategory = publicationCategory.toTransportPublication(),
    visibility = visibility.toTransportPublication(),
)

fun MkplPublication.toTransportRead() = PublicationReadObject(
    id = id.takeIf { it != MkplPublicationId.NONE }?.asString(),
)

fun MkplPublication.toTransportUpdate() = PublicationUpdateObject(
    id = id.takeIf { it != MkplPublicationId.NONE }?.asString(),
    title = title.takeIf { it.isNotBlank() },
    description = description.takeIf { it.isNotBlank() },
    publicationCategory = publicationCategory.toTransportPublication(),
    visibility = visibility.toTransportPublication(),
    lock = lock.takeIf { it != MkplPublicationLock.NONE }?.asString(),
)

fun MkplPublication.toTransportDelete() = PublicationDeleteObject(
    id = id.takeIf { it != MkplPublicationId.NONE }?.asString(),
    lock = lock.takeIf { it != MkplPublicationLock.NONE }?.asString(),
)