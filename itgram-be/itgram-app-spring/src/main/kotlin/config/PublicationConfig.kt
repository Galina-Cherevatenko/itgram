package ru.itgram.app.spring.config

import PublicationRepoInMemory
import PublicationRepoStub
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.itgram.backend.repo.postgresql.RepoPublicationSql
import ru.itgram.biz.MkplPublicationProcessor
import ru.itgram.common.MkplCorSettings
import ru.itgram.common.repo.IRepoPublication

@Suppress("unused")
@EnableConfigurationProperties(PublicationConfigPostgres::class)
@Configuration
class PublicationConfig(val postgresConfig: PublicationConfigPostgres) {

    @Bean
    fun processor(corSettings: MkplCorSettings) = MkplPublicationProcessor(corSettings = corSettings)

    @Bean
    fun testRepo(): IRepoPublication = PublicationRepoInMemory()

    @Bean
    fun prodRepo(): IRepoPublication = RepoPublicationSql(postgresConfig.psql)

    @Bean
    fun stubRepo(): IRepoPublication = PublicationRepoStub()

    @Bean
    fun corSettings(): MkplCorSettings = MkplCorSettings(
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
