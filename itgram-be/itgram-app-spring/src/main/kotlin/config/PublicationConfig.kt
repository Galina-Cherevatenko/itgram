package ru.itgram.app.spring.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import MkplPublicationProcessor
import ru.itgram.common.MkplCorSettings
import ru.itgram.logging.common.MpLoggerProvider
import ru.itgram.logging.jvm.mpLoggerLogback

@Suppress("unused")
@Configuration
class PublicationConfig  {
    @Bean
    fun processor(corSettings: MkplCorSettings) = MkplPublicationProcessor(corSettings = corSettings)

    @Bean
    fun loggerProvider(): MpLoggerProvider = MpLoggerProvider { mpLoggerLogback(it) }

    @Bean
    fun corSettings(): MkplCorSettings = MkplCorSettings(
        loggerProvider = loggerProvider(),
    )

    @Bean
    fun appSettings(
        corSettings: MkplCorSettings,
        processor: MkplPublicationProcessor,
    ) = MkplAppSettings(
        corSettings = corSettings,
        processor = processor,
    )
}
