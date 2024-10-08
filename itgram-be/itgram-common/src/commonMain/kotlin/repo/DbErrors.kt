package ru.itgram.common.repo

import ru.itgram.common.models.MkplError
import ru.itgram.common.models.MkplPublicationId

const val ERROR_GROUP_REPO = "repo"

fun errorNotFound(id: MkplPublicationId) = DbPublicationResponseErr(
    MkplError(
        code = "$ERROR_GROUP_REPO-not-found",
        group = ERROR_GROUP_REPO,
        field = "id",
        message = "Object with ID: ${id.asString()} is not Found",
    )
)

val errorEmptyId = DbPublicationResponseErr(
    MkplError(
        code = "$ERROR_GROUP_REPO-empty-id",
        group = ERROR_GROUP_REPO,
        field = "id",
        message = "Id must not be null or blank"
    )
)