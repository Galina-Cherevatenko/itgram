package ru.itgram.biz.validation

import ru.itgram.common.MkplContext
import ru.itgram.common.models.MkplState
import ru.itgram.cor.ICorChainDsl
import ru.itgram.cor.worker

fun ICorChainDsl<MkplContext>.finishPublicationValidation(title: String) = worker {
    this.title = title
    on { state == MkplState.RUNNING }
    handle {
        publicationValidated = publicationValidating
    }
}

fun ICorChainDsl<MkplContext>.finishAdFilterValidation(title: String) = worker {
    this.title = title
    on { state == MkplState.RUNNING }
    handle {
        publicationFilterValidated = publicationFilterValidating
    }
}