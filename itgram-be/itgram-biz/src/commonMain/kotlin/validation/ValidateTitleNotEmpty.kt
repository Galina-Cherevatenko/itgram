package ru.itgram.biz.validation

import ru.itgram.common.MkplContext
import ru.itgram.common.helpers.errorValidation
import ru.itgram.common.helpers.fail
import ru.itgram.cor.ICorChainDsl
import ru.itgram.cor.worker

fun ICorChainDsl<MkplContext>.validateTitleNotEmpty(title: String) = worker {
    this.title = title
    on { publicationValidating.title.isEmpty() }
    handle {
        fail(
            errorValidation(
                field = "title",
                violationCode = "empty",
                description = "field must not be empty"
            )
        )
    }
}