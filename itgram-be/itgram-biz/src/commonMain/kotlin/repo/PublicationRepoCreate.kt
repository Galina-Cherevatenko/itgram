package ru.itgram.biz.repository

import ru.itgram.common.MkplContext
import ru.itgram.common.helpers.fail
import ru.itgram.common.models.MkplState
import ru.itgram.common.repo.DbPublicationRequest
import ru.itgram.common.repo.DbPublicationResponseErr
import ru.itgram.common.repo.DbPublicationResponseErrWithData
import ru.itgram.common.repo.DbPublicationResponseOk
import ru.itgram.cor.ICorChainDsl
import ru.itgram.cor.worker

fun ICorChainDsl<MkplContext>.repoCreate(title: String) = worker {
    this.title = title
    description = "Добавление публикации в БД"
    on { state == MkplState.RUNNING }
    handle {
        val request = DbPublicationRequest(publicationRepoPrepare)
        when(val result = publicationRepo.createPublication(request)) {
            is DbPublicationResponseOk -> publicationRepoDone = result.data
            is DbPublicationResponseErr -> fail(result.errors)
            is DbPublicationResponseErrWithData -> fail(result.errors)
        }
    }
}