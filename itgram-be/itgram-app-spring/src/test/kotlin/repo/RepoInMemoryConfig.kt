package ru.itgram.app.spring.repo

import PublicationRepoInMemory
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import ru.itgram.common.repo.IRepoPublication

@TestConfiguration
class RepoInMemoryConfig {
    @Suppress("unused")
    @Bean()
    @Primary
    fun prodRepo(): IRepoPublication = PublicationRepoInMemory()
}