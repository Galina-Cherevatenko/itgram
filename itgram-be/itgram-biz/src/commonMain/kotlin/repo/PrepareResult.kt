package ru.itgram.biz.repository

import ru.itgram.common.MkplContext
import ru.itgram.common.models.MkplState
import ru.itgram.common.models.MkplWorkMode
import ru.itgram.cor.ICorChainDsl
import ru.itgram.cor.worker

fun ICorChainDsl<MkplContext>.prepareResult(title: String) = worker {
    this.title = title
    description = "Подготовка данных для ответа клиенту на запрос"
    on { workMode != MkplWorkMode.STUB }
    handle {
        publicationResponse = publicationRepoDone
        publicationsResponse = publicationsRepoDone
        state = when (val st = state) {
            MkplState.RUNNING -> MkplState.FINISHING
            else -> st
        }
    }
}