package stubs

import ru.itgram.common.MkplContext
import ru.itgram.common.helpers.fail
import ru.itgram.common.models.MkplState
import ru.itgram.common.models.MkplError
import ru.itgram.cor.ICorChainDsl
import ru.itgram.cor.worker

fun ICorChainDsl<MkplContext>.stubNoCase(title: String) = worker {
    this.title = title
    this.description = """
        Валидируем ситуацию, когда запрошен кейс, который не поддерживается в стабах
    """.trimIndent()
    on { state == MkplState.RUNNING }
    handle {
        fail(
            MkplError(
                code = "validation",
                field = "stub",
                group = "validation",
                message = "Wrong stub case is requested: ${stubCase.name}"
            )
        )
    }
}