package ru.itgram.biz.stubs

import ru.itgram.common.MkplContext
import ru.itgram.common.helpers.fail
import ru.itgram.common.models.MkplError
import ru.itgram.common.models.MkplState
import ru.itgram.common.stubs.MkplStubs
import ru.itgram.cor.ICorChainDsl
import ru.itgram.cor.worker

fun ICorChainDsl<MkplContext>.stubValidationBadTitle(title: String) = worker {
    this.title = title
    this.description = """
        Кейс ошибки валидации для заголовка
    """.trimIndent()

    on { stubCase == MkplStubs.BAD_TITLE && state == MkplState.RUNNING }
    handle {
        fail(
            MkplError(
                group = "validation",
                code = "validation-title",
                field = "title",
                message = "Wrong title field"
            )
        )
    }
}