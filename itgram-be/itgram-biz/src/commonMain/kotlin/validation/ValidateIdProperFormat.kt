package ru.itgram.biz.validation

import ru.itgram.common.MkplContext
import ru.itgram.common.helpers.errorValidation
import ru.itgram.common.helpers.fail
import ru.itgram.common.models.MkplPublicationId
import ru.itgram.cor.ICorChainDsl
import ru.itgram.cor.worker

fun ICorChainDsl<MkplContext>.validateIdProperFormat(title: String) = worker {
    this.title = title
    val regExp = Regex("^[0-9a-zA-Z#:-]+$")
    on { publicationValidating.id != MkplPublicationId.NONE && ! publicationValidating.id.asString().matches(regExp) }
    handle {
        val encodedId = publicationValidating.id.asString()
            .replace("<", "&lt;")
            .replace(">", "&gt;")
        fail(
            errorValidation(
                field = "id",
                violationCode = "badFormat",
                description = "value $encodedId must contain only letters and numbers"
            )
        )
    }
}