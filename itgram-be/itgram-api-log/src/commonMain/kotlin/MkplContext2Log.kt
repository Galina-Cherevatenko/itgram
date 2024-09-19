
import kotlinx.datetime.Clock
import ru.itgram.api.log.models.CommonLogModel
import ru.itgram.api.log.models.ErrorLogModel
import ru.itgram.api.log.models.MkplPublicationLogModel
import ru.itgram.api.log.models.PublicationLog
import ru.itgram.common.MkplContext
import ru.itgram.common.models.*

fun MkplContext.toLog(logId: String) = CommonLogModel(
    messageTime = Clock.System.now().toString(),
    logId = logId,
    source = "itgram",
    publication = toMkplLog(),
    errors = errors.map { it.toLog() },
)

private fun MkplContext.toMkplLog(): MkplPublicationLogModel? {
    val publicationNone = MkplPublication()
    return MkplPublicationLogModel(
        requestId = requestId.takeIf { it != MkplRequestId.NONE }?.asString(),
        requestPublication = publicationRequest.takeIf { it != publicationNone }?.toLog(),
        responsePublication = publicationResponse.takeIf { it != publicationNone }?.toLog(),
        responsePublications = publicationsResponse.takeIf { it.isNotEmpty() }?.filter { it != publicationNone }?.map { it.toLog() },
    ).takeIf { it != MkplPublicationLogModel() }
}

private fun MkplError.toLog() = ErrorLogModel(
    message = message.takeIf { it.isNotBlank() },
    field = field.takeIf { it.isNotBlank() },
    code = code.takeIf { it.isNotBlank() },
    level = level.name,
)

private fun MkplPublication.toLog() = PublicationLog(
    id = id.takeIf { it != MkplPublicationId.NONE }?.asString(),
    title = title.takeIf { it.isNotBlank() },
    description = description.takeIf { it.isNotBlank() },
    publicationCategory = publicationCategory.name,
    visibility = visibility.takeIf { it != MkplVisibility.NONE }?.name,
    ownerId = ownerId.takeIf { it != MkplUserId.NONE }?.asString(),
    permissions = permissionsClient.takeIf { it.isNotEmpty() }?.map { it.name }?.toSet(),
)