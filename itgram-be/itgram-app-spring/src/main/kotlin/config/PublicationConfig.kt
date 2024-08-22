package config

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
