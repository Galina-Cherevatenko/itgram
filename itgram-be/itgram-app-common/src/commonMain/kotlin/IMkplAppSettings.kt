import ru.itgram.biz.MkplPublicationProcessor
import ru.itgram.common.MkplCorSettings

interface IMkplAppSettings {
    val processor: MkplPublicationProcessor
    val corSettings: MkplCorSettings
}