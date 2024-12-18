package ru.itgram.common.repo

import ru.itgram.common.models.MkplPublication
import ru.itgram.common.models.MkplPublicationId
import ru.itgram.common.models.MkplPublicationLock

data class DbPublicationIdRequest(
    val id: MkplPublicationId,
    val lock: MkplPublicationLock = MkplPublicationLock.NONE,
) {
    constructor(publication: MkplPublication): this(publication.id, publication.lock)
}