package config

import ru.itgram.app.common.IMkplAppSettings
import ru.itgram.biz.MkplPublicationProcessor
import ru.itgram.common.MkplCorSettings

data class MkplAppSettings(
    override val corSettings: MkplCorSettings = MkplCorSettings(),
    override val processor: MkplPublicationProcessor = MkplPublicationProcessor(corSettings),
    override val rabbit: RabbitConfig = RabbitConfig(),
    override val controllersConfig: RabbitExchangeConfiguration = RabbitExchangeConfiguration.NONE
): IMkplAppSettings, IMkplAppRabbitSettings