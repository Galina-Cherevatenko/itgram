package ru.itgram.common.repo

import ru.itgram.common.helpers.errorSystem
import ru.itgram.common.models.MkplError
import ru.itgram.common.models.MkplPublication
import ru.itgram.common.models.MkplPublicationId
import ru.itgram.common.models.MkplPublicationLock
import ru.itgram.common.repo.exceptions.RepoConcurrencyException
import ru.itgram.common.repo.exceptions.RepoException

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

fun errorRepoConcurrency(
    oldPublication: MkplPublication,
    expectedLock: MkplPublicationLock,
    exception: Exception = RepoConcurrencyException(
        id = oldPublication.id,
        expectedLock = expectedLock,
        actualLock = oldPublication.lock,
    ),
) = DbPublicationResponseErrWithData(
    publication = oldPublication,
    err = MkplError(
        code = "$ERROR_GROUP_REPO-concurrency",
        group = ERROR_GROUP_REPO,
        field = "lock",
        message = "The object with ID ${oldPublication.id.asString()} has been changed concurrently by another user or process",
        exception = exception,
    )
)

fun errorEmptyLock(id: MkplPublicationId) = DbPublicationResponseErr(
    MkplError(
        code = "$ERROR_GROUP_REPO-lock-empty",
        group = ERROR_GROUP_REPO,
        field = "lock",
        message = "Lock for Publication ${id.asString()} is empty that is not admitted"
    )
)

fun errorDb(e: RepoException) = DbPublicationResponseErr(
    errorSystem(
        violationCode = "dbLockEmpty",
        e = e
    )
)
