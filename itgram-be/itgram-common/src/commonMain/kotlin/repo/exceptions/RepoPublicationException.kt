package ru.itgram.common.repo.exceptions

import ru.itgram.common.models.MkplPublicationId

open class RepoPublicationException(
    @Suppress("unused")
    val publicationId: MkplPublicationId,
    msg: String,
): RepoException(msg)