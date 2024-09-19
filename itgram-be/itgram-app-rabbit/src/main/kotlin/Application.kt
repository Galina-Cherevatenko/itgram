import config.MkplAppSettings
import config.RabbitConfig
import kotlinx.coroutines.runBlocking
import mappers.fromArgs
import ru.itgram.common.MkplCorSettings
import ru.itgram.logging.common.MpLoggerProvider
import ru.itgram.logging.jvm.mpLoggerLogback

fun main(vararg args: String) = runBlocking {
    val appSettings = MkplAppSettings(
        rabbit = RabbitConfig.fromArgs(*args),
        corSettings = MkplCorSettings(
            loggerProvider = MpLoggerProvider { mpLoggerLogback(it) }
        )
    )
    val app = RabbitApp(appSettings = appSettings, this)
    app.start()
}