package ru.itgram.biz.repository

import ru.itgram.common.MkplContext
import ru.itgram.common.models.MkplState
import ru.itgram.cor.ICorChainDsl
import ru.itgram.cor.worker

fun ICorChainDsl<MkplContext>.repoPrepareDelete(title: String) = worker {
    this.title = title
    description = """
        Готовим данные к удалению из БД
    """.trimIndent()
    on { state == MkplState.RUNNING }
    handle {
        publicationRepoPrepare = publicationValidated.deepCopy()
    }
}