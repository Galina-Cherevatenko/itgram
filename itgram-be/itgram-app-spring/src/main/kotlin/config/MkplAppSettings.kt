package config

import IMkplAppSettings
import ru.itgram.biz.MkplPublicationProcessor

class MkplAppSettings(
    override val corSettings: MkplCorSettings,
    override val processor: MkplPublicationProcessor,
): IMkplAppSettings