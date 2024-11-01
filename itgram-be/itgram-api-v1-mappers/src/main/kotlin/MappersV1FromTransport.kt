package src.main.kotlin

import ru.itgram.api.v1.models.*
import ru.itgram.common.MkplContext
import src.main.kotlin.exceptions.UnknownRequestClass
import ru.itgram.common.models.*
import ru.itgram.common.stubs.MkplStubs

fun MkplContext.fromTransport(request: IRequest) = when (request) {
    is PublicationCreateRequest -> fromTransport(request)
    is PublicationReadRequest -> fromTransport(request)
    is PublicationUpdateRequest -> fromTransport(request)
    is PublicationDeleteRequest -> fromTransport(request)
    is PublicationSearchRequest -> fromTransport(request)
    else -> throw UnknownRequestClass(request.javaClass)
}

private fun String?.toPublicationId() = this?.let { MkplPublicationId(it) } ?: MkplPublicationId.NONE
private fun String?.toPublicationLock() = this?.let { MkplPublicationLock(it) } ?: MkplPublicationLock.NONE

private fun PublicationDebug?.transportToWorkMode(): MkplWorkMode = when (this?.mode) {
    PublicationRequestDebugMode.PROD -> MkplWorkMode.PROD
    PublicationRequestDebugMode.TEST -> MkplWorkMode.TEST
    PublicationRequestDebugMode.STUB -> MkplWorkMode.STUB
    null -> MkplWorkMode.PROD
}

private fun PublicationDebug?.transportToStubCase(): MkplStubs = when (this?.stub) {
    PublicationRequestDebugStubs.SUCCESS -> MkplStubs.SUCCESS
    PublicationRequestDebugStubs.NOT_FOUND -> MkplStubs.NOT_FOUND
    PublicationRequestDebugStubs.BAD_ID -> MkplStubs.BAD_ID
    PublicationRequestDebugStubs.BAD_TITLE -> MkplStubs.BAD_TITLE
    PublicationRequestDebugStubs.BAD_DESCRIPTION -> MkplStubs.BAD_DESCRIPTION
    PublicationRequestDebugStubs.BAD_VISIBILITY -> MkplStubs.BAD_VISIBILITY
    PublicationRequestDebugStubs.CANNOT_DELETE -> MkplStubs.CANNOT_DELETE
    PublicationRequestDebugStubs.BAD_SEARCH_STRING -> MkplStubs.BAD_SEARCH_STRING
    null -> MkplStubs.NONE
}

fun MkplContext.fromTransport(request: PublicationCreateRequest) {
    command = MkplCommand.CREATE
    publicationRequest = request.publication?.toInternal() ?: MkplPublication()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun MkplContext.fromTransport(request: PublicationReadRequest) {
    command = MkplCommand.READ
    publicationRequest = request.publication.toInternal()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

private fun PublicationReadObject?.toInternal(): MkplPublication = if (this != null) {
    MkplPublication(id = id.toPublicationId())
} else {
    MkplPublication()
}


fun MkplContext.fromTransport(request: PublicationUpdateRequest) {
    command = MkplCommand.UPDATE
    publicationRequest = request.publication?.toInternal() ?: MkplPublication()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun MkplContext.fromTransport(request: PublicationDeleteRequest) {
    command = MkplCommand.DELETE
    publicationRequest = request.publication.toInternal()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

private fun PublicationDeleteObject?.toInternal(): MkplPublication = if (this != null) {
    MkplPublication(
        id = id.toPublicationId(),
        lock = lock.toPublicationLock(),
    )
} else {
    MkplPublication()
}

fun MkplContext.fromTransport(request: PublicationSearchRequest) {
    command = MkplCommand.SEARCH
    publicationFilterRequest = request.publicationFilter.toInternal()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

private fun PublicationSearchFilter?.toInternal(): MkplPublicationFilter = MkplPublicationFilter(
    searchString = this?.searchString ?: "",
    ownerId = this?.ownerId?.let { MkplUserId(it) } ?: MkplUserId.NONE,
    publicationCategory = this?.publicationCategory.fromTransport(),
)

private fun PublicationCreateObject.toInternal(): MkplPublication = MkplPublication(
    title = this.title ?: "",
    description = this.description ?: "",
    publicationCategory = this.publicationCategory.fromTransport(),
    visibility = this.visibility.fromTransport(),
)

private fun PublicationUpdateObject.toInternal(): MkplPublication = MkplPublication(
    id = this.id.toPublicationId(),
    title = this.title ?: "",
    description = this.description ?: "",
    publicationCategory = this.publicationCategory.fromTransport(),
    visibility = this.visibility.fromTransport(),
    lock = lock.toPublicationLock(),
)

private fun PublicationVisibility?.fromTransport(): MkplVisibility = when (this) {
    PublicationVisibility.PUBLIC -> MkplVisibility.VISIBLE_PUBLIC
    PublicationVisibility.OWNER_ONLY -> MkplVisibility.VISIBLE_TO_OWNER
    PublicationVisibility.REGISTERED_ONLY -> MkplVisibility.VISIBLE_TO_GROUP
    null -> MkplVisibility.NONE
}

private fun PublicationCategory?.fromTransport(): MkplPublicationCategory = when (this) {
    PublicationCategory.POST -> MkplPublicationCategory.POST
    PublicationCategory.START -> MkplPublicationCategory.START
    PublicationCategory.AD -> MkplPublicationCategory.AD
    null -> MkplPublicationCategory.POST
}