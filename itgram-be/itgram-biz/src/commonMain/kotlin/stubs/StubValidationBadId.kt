package ru.itgram.biz.stubs

import ru.itgram.common.MkplContext
import ru.itgram.common.helpers.fail
import ru.itgram.common.models.MkplError
import ru.itgram.common.models.MkplState
import ru.itgram.common.stubs.MkplStubs
import ru.itgram.cor.ICorChainDsl
import ru.itgram.cor.worker

fun ICorChainDsl<MkplContext>.stubValidationBadId(title: String) = worker {
    this.title = title
    this.description = """
        Кейс ошибки валидации для идентификатора публикации
    """.trimIndent()
    on { stubCase == MkplStubs.BAD_ID && state == MkplState.RUNNING }
    handle {
        fail(
            MkplError(
                group = "validation",
                code = "validation-id",
                field = "id",
                message = "Wrong id field"
            )
        )
    }
}