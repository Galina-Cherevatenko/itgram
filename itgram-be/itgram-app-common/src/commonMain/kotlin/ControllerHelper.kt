import kotlinx.datetime.Clock
import ru.itgram.common.MkplContext
import ru.itgram.common.helpers.asMkplError
import ru.itgram.common.models.MkplState
import kotlin.reflect.KClass

suspend inline fun <T> IMkplAppSettings.controllerHelper(
    crossinline getRequest: suspend MkplContext.() -> Unit,
    crossinline toResponse: suspend MkplContext.() -> T,
    clazz: KClass<*>,
    logId: String,
): T {
    val logger = corSettings.loggerProvider.logger(clazz)
    val ctx = MkplContext(
        timeStart = Clock.System.now(),
    )
    return try {
        ctx.getRequest()
        logger.info(
            msg = "Request $logId started for ${clazz.simpleName}",
            marker = "BIZ",
            data = ctx.toLog(logId)
        )
        processor.exec(ctx)
        logger.info(
            msg = "Request $logId processed for ${clazz.simpleName}",
            marker = "BIZ",
            data = ctx.toLog(logId)
        )
        ctx.toResponse()
    } catch (e: Throwable) {
        logger.error(
            msg = "Request $logId failed for ${clazz.simpleName}",
            marker = "BIZ",
            data = ctx.toLog(logId)
        )
        ctx.state = MkplState.FAILING
        ctx.errors.add(e.asMkplError())
        processor.exec(ctx)
        ctx.toResponse()
    }
}