package config

import ru.itgram.app.common.IMkplAppSettings

interface IMkplAppRabbitSettings: IMkplAppSettings {
    val rabbit: RabbitConfig
    val controllersConfig: RabbitExchangeConfiguration
}