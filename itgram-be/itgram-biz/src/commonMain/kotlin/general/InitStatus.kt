package ru.itgram.biz.general

import ru.itgram.common.MkplContext
import ru.itgram.common.models.MkplState
import ru.itgram.cor.ICorChainDsl
import ru.itgram.cor.worker

fun ICorChainDsl<MkplContext>.initStatus(title: String) = worker() {
    this.title = title
    this.description = """
        Этот обработчик устанавливает стартовый статус обработки. Запускается только в случае не заданного статуса.
    """.trimIndent()
    on { state == MkplState.NONE }
    handle { state = MkplState.RUNNING }
}