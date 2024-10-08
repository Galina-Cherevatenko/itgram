package ru.itgram.biz.validation

import PublicationRepoInMemory
import PublicationRepoInitialized
import ru.itgram.biz.MkplPublicationProcessor
import ru.itgram.common.MkplCorSettings
import ru.itgram.common.models.MkplCommand

abstract class BaseBizValidationTest {
    protected abstract val command: MkplCommand
    private val repo = PublicationRepoInitialized(
        repo = PublicationRepoInMemory(),
        initObjects = listOf(
            MkplPublicationStub.get(),
        ),
    )
    private val settings by lazy { MkplCorSettings(repoTest = repo) }
    protected val processor by lazy { MkplPublicationProcessor(settings) }
}