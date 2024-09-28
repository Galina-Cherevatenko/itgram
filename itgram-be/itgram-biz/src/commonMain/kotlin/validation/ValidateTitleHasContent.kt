package ru.itgram.biz.validation

import ru.itgram.common.MkplContext
import ru.itgram.common.helpers.errorValidation
import ru.itgram.common.helpers.fail
import ru.itgram.cor.ICorChainDsl
import ru.itgram.cor.worker

fun ICorChainDsl<MkplContext>.validateTitleHasContent(title: String) = worker {
    this.title = title
    this.description = """
        Проверяем, что у нас есть какие-то слова в заголовке.
        Отказываем в публикации заголовков, в которых только бессмысленные символы типа %^&^$^%#^))&^*&%^^&
    """.trimIndent()
    val regExp = Regex("\\p{L}")
    on { publicationValidating.title.isNotEmpty() && ! publicationValidating.title.contains(regExp) }
    handle {
        fail(
            errorValidation(
                field = "title",
                violationCode = "noContent",
                description = "field must contain letters"
            )
        )
    }
}