package general

import ru.itgram.common.MkplContext
import ru.itgram.common.models.MkplCommand
import ru.itgram.common.models.MkplState
import ru.itgram.cor.ICorChainDsl
import ru.itgram.cor.chain

fun ICorChainDsl<MkplContext>.operation(
    title: String,
    command: MkplCommand,
    block: ICorChainDsl<MkplContext>.() -> Unit
) = chain {
    block()
    this.title = title
    on { this.command == command && state == MkplState.RUNNING }
}