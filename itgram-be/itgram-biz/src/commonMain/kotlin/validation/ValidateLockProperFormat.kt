package ru.itgram.biz.validation

import ru.itgram.common.MkplContext
import ru.itgram.common.helpers.errorValidation
import ru.itgram.common.helpers.fail
import ru.itgram.common.models.MkplPublicationLock
import ru.itgram.cor.ICorChainDsl
import ru.itgram.cor.worker

fun ICorChainDsl<MkplContext>.validateLockProperFormat(title: String) = worker {
    this.title = title
    val regExp = Regex("^[0-9a-zA-Z-]+$")
    on { publicationValidating.lock != MkplPublicationLock.NONE && !publicationValidating.lock.asString().matches(regExp) }
    handle {
        val encodedId = publicationValidating.lock.asString()
        fail(
            errorValidation(
                field = "lock",
                violationCode = "badFormat",
                description = "value $encodedId must contain only"
            )
        )
    }
}