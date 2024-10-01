package ru.itgram.biz.validation

import ru.itgram.common.MkplContext
import ru.itgram.common.models.MkplState
import ru.itgram.cor.ICorChainDsl
import ru.itgram.cor.chain

fun ICorChainDsl<MkplContext>.validation(block: ICorChainDsl<MkplContext>.() -> Unit) = chain {
    block()
    title = "Валидация"

    on { state == MkplState.RUNNING }
}