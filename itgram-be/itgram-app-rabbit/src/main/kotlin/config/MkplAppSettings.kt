package config

import IMkplAppSettings
import MkplPublicationProcessor
import ru.itgram.common.MkplCorSettings

data class MkplAppSettings(
    override val corSettings: MkplCorSettings = MkplCorSettings(),
    override val processor: MkplPublicationProcessor = MkplPublicationProcessor(corSettings),
    override val rabbit: RabbitConfig = RabbitConfig(),
    override val controllersConfig: RabbitExchangeConfiguration = RabbitExchangeConfiguration.NONE,
): IMkplAppSettings, IMkplAppRabbitSettings