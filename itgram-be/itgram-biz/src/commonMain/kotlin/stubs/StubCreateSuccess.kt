package stubs

import ru.itgram.common.MkplContext
import ru.itgram.common.MkplCorSettings
import ru.itgram.common.models.MkplState
import ru.itgram.common.models.MkplVisibility
import ru.itgram.common.stubs.MkplStubs
import ru.itgram.logging.common.LogLevel
import ru.itgram.cor.ICorChainDsl
import ru.itgram.cor.worker

fun ICorChainDsl<MkplContext>.stubCreateSuccess(title: String, corSettings: MkplCorSettings) = worker {
    this.title = title
    this.description = """
        Кейс успеха для создания публикации
    """.trimIndent()
    on { stubCase == MkplStubs.SUCCESS && state == MkplState.RUNNING }
    val logger = corSettings.loggerProvider.logger("stubOffersSuccess")
    handle {
        logger.doWithLogging(id = this.requestId.asString(), LogLevel.DEBUG) {
            state = MkplState.FINISHING
            val stub = MkplPublicationStub.prepareResult {
                publicationRequest.title.takeIf { it.isNotBlank() }?.also { this.title = it }
                publicationRequest.description.takeIf { it.isNotBlank() }?.also { this.description = it }
                publicationRequest.publicationCategory.also { this.publicationCategory = it }
                publicationRequest.visibility.takeIf { it != MkplVisibility.NONE }?.also { this.visibility = it }
            }
            publicationResponse = stub
        }
    }
}