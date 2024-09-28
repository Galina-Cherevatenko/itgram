package ru.itgram.biz.validation

import MkplPublicationProcessor
import ru.itgram.common.MkplCorSettings
import ru.itgram.common.models.MkplCommand

abstract class BaseBizValidationTest {
    protected abstract val command: MkplCommand
    private val settings by lazy { MkplCorSettings() }
    protected val processor by lazy { MkplPublicationProcessor(settings) }
}