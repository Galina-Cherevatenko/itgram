package ru.itgram.common.repo

import ru.itgram.common.models.MkplPublication
import ru.itgram.common.models.MkplPublicationId

data class DbPublicationIdRequest(
    val id: MkplPublicationId,
) {
    constructor(publication: MkplPublication): this(publication.id)
}