package src.main.kotlin

import ru.itgram.api.v1.models.*
import ru.itgram.common.MkplContext
import ru.itgram.common.exceptions.UnknownMkplCommand
import ru.itgram.common.models.*

fun MkplContext.toTransportPublication(): IResponse = when (val cmd = command) {
    MkplCommand.CREATE -> toTransportCreate()
    MkplCommand.READ -> toTransportRead()
    MkplCommand.UPDATE -> toTransportUpdate()
    MkplCommand.DELETE -> toTransportDelete()
    MkplCommand.SEARCH -> toTransportSearch()
    MkplCommand.NONE -> throw UnknownMkplCommand(cmd)
}

fun MkplContext.toTransportCreate() = PublicationCreateResponse(
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    publication = publicationResponse.toTransportPublication()
)

fun MkplContext.toTransportRead() = PublicationReadResponse(
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    publication = publicationResponse.toTransportPublication()
)

fun MkplContext.toTransportUpdate() = PublicationUpdateResponse(
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    publication = publicationResponse.toTransportPublication()
)

fun MkplContext.toTransportDelete() = PublicationDeleteResponse(
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    publication = publicationResponse.toTransportPublication()
)

fun MkplContext.toTransportSearch() = PublicationSearchResponse(
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    publications = publicationsResponse.toTransportPublication()
)

fun List<MkplPublication>.toTransportPublication(): List<PublicationResponseObject>? = this
    .map { it.toTransportPublication() }
    .toList()
    .takeIf { it.isNotEmpty() }

private fun MkplPublication.toTransportPublication(): PublicationResponseObject = PublicationResponseObject(
    id = id.takeIf { it != MkplPublicationId.NONE }?.asString(),
    title = title.takeIf { it.isNotBlank() },
    description = description.takeIf { it.isNotBlank() },
    ownerId = ownerId.takeIf { it != MkplUserId.NONE }?.asString(),
    publicationCategory  = publicationCategory.toTransportPublication(),
    visibility = visibility.toTransportPublication(),
    permissions = permissionsClient.toTransportPublication(),
)

private fun Set<MkplPublicationPermissionClient>.toTransportPublication(): Set<PublicationPermissions>? = this
    .map { it.toTransportPublication() }
    .toSet()
    .takeIf { it.isNotEmpty() }

private fun MkplPublicationPermissionClient.toTransportPublication() = when (this) {
    MkplPublicationPermissionClient.READ -> PublicationPermissions.READ
    MkplPublicationPermissionClient.UPDATE -> PublicationPermissions.UPDATE
    MkplPublicationPermissionClient.MAKE_VISIBLE_OWNER -> PublicationPermissions.MAKE_VISIBLE_OWN
    MkplPublicationPermissionClient.MAKE_VISIBLE_GROUP -> PublicationPermissions.MAKE_VISIBLE_GROUP
    MkplPublicationPermissionClient.MAKE_VISIBLE_PUBLIC -> PublicationPermissions.MAKE_VISIBLE_PUBLIC
    MkplPublicationPermissionClient.DELETE -> PublicationPermissions.DELETE
}

private fun MkplVisibility.toTransportPublication(): PublicationVisibility? = when (this) {
    MkplVisibility.VISIBLE_PUBLIC -> PublicationVisibility.PUBLIC
    MkplVisibility.VISIBLE_TO_GROUP -> PublicationVisibility.REGISTERED_ONLY
    MkplVisibility.VISIBLE_TO_OWNER -> PublicationVisibility.OWNER_ONLY
    MkplVisibility.NONE -> null
}

private fun MkplPublicationCategory.toTransportPublication(): PublicationCategory? = when (this) {
    MkplPublicationCategory.POST -> PublicationCategory.POST
    MkplPublicationCategory.AD -> PublicationCategory.AD
    MkplPublicationCategory.START -> PublicationCategory.START
}

private fun List<MkplError>.toTransportErrors(): List<Error>? = this
    .map { it.toTransportPublication() }
    .toList()
    .takeIf { it.isNotEmpty() }

private fun MkplError.toTransportPublication() = Error(
    code = code.takeIf { it.isNotBlank() },
    group = group.takeIf { it.isNotBlank() },
    field = field.takeIf { it.isNotBlank() },
    message = message.takeIf { it.isNotBlank() },
)

private fun MkplState.toResult(): ResponseResult? = when (this) {
    MkplState.RUNNING -> ResponseResult.SUCCESS
    MkplState.FAILING -> ResponseResult.ERROR
    MkplState.FINISHING -> ResponseResult.SUCCESS
    MkplState.NONE -> null
}
