package ru.itgram.app.spring.config

import PublicationRepoInMemory
import PublicationRepoStub
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.itgram.biz.MkplPublicationProcessor
import ru.itgram.common.MkplCorSettings
import ru.itgram.common.repo.IRepoPublication
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
    fun testRepo(): IRepoPublication = PublicationRepoInMemory()

    @Bean
    fun prodRepo(): IRepoPublication = PublicationRepoInMemory()

    @Bean
    fun stubRepo(): IRepoPublication = PublicationRepoStub()

    @Bean
    fun corSettings(): MkplCorSettings = MkplCorSettings(
        loggerProvider = loggerProvider(),
        repoTest = testRepo(),
        repoProd = prodRepo(),
        repoStub = stubRepo(),
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
