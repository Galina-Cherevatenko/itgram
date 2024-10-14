package ru.itgram.biz.repository

import ru.itgram.common.MkplContext
import ru.itgram.common.models.MkplState
import ru.itgram.cor.ICorChainDsl
import ru.itgram.cor.worker

fun ICorChainDsl<MkplContext>.repoPrepareUpdate(title: String) = worker {
    this.title = title
    description = "Готовим данные к сохранению в БД: совмещаем данные, прочитанные из БД, " +
            "и данные, полученные от пользователя"
    on { state == MkplState.RUNNING }
    handle {
        publicationRepoPrepare = publicationRepoRead.deepCopy().apply {
            this.title = publicationValidated.title
            description = publicationValidated.description
            publicationCategory = publicationValidated.publicationCategory
            visibility = publicationValidated.visibility
            lock = publicationValidated.lock
        }
    }
}