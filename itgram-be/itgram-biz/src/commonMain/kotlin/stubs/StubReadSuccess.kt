package ru.itgram.biz.stubs

import ru.itgram.common.MkplContext
import ru.itgram.common.MkplCorSettings
import ru.itgram.common.models.MkplState
import ru.itgram.common.stubs.MkplStubs
import ru.itgram.cor.ICorChainDsl
import ru.itgram.cor.worker
import ru.itgram.logging.common.LogLevel

fun ICorChainDsl<MkplContext>.stubReadSuccess(title: String, corSettings: MkplCorSettings) = worker {
    this.title = title
    this.description = """
        Кейс успеха для чтения публикации
    """.trimIndent()
    on { stubCase == MkplStubs.SUCCESS && state == MkplState.RUNNING }
    val logger = corSettings.loggerProvider.logger("stubOffersSuccess")
    handle {
        logger.doWithLogging(id = this.requestId.asString(), LogLevel.DEBUG) {
            var state = MkplState.FINISHING
            val stub = MkplPublicationStub.prepareResult {
                publicationRequest.title.takeIf { it.isNotBlank() }?.also { this.title = it }
            }
            publicationResponse = stub
        }
    }
}