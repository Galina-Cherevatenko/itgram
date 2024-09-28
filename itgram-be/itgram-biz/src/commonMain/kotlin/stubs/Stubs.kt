package stubs

import ru.itgram.common.MkplContext
import ru.itgram.common.models.MkplState
import ru.itgram.common.models.MkplWorkMode
import ru.itgram.cor.ICorChainDsl
import ru.itgram.cor.chain

fun ICorChainDsl<MkplContext>.stubs(title: String, block: ICorChainDsl<MkplContext>.() -> Unit) = chain {
    block()
    this.title = title
    on { workMode == MkplWorkMode.STUB && state == MkplState.RUNNING }
}