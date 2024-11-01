package ru.itgram.biz.repository

import ru.itgram.common.MkplContext
import ru.itgram.common.models.MkplState
import ru.itgram.common.models.MkplUserId
import ru.itgram.cor.ICorChainDsl
import ru.itgram.cor.worker

fun ICorChainDsl<MkplContext>.repoPrepareCreate(title: String) = worker {
    this.title = title
    description = "Подготовка объекта к сохранению в базе данных"
    on { state == MkplState.RUNNING }
    handle {
        publicationRepoPrepare = publicationValidated.deepCopy()
        // TODO будет реализовано в занятии по управлению пользвателями
        publicationRepoPrepare.ownerId = MkplUserId.NONE
    }
}