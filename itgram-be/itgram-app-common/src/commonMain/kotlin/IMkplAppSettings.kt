import ru.itgram.biz.MkplPublicationProcessor

interface IMkplAppSettings {
    val processor: MkplPublicationProcessor
    val corSettings: MkplCorSettings
}