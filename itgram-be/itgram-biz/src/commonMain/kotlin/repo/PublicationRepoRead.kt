package ru.itgram.biz.repository

import ru.itgram.common.MkplContext
import ru.itgram.common.helpers.fail
import ru.itgram.common.models.MkplState
import ru.itgram.common.repo.DbPublicationIdRequest
import ru.itgram.common.repo.DbPublicationResponseErr
import ru.itgram.common.repo.DbPublicationResponseErrWithData
import ru.itgram.common.repo.DbPublicationResponseOk
import ru.itgram.cor.ICorChainDsl
import ru.itgram.cor.worker

fun ICorChainDsl<MkplContext>.repoRead(title: String) = worker {
    this.title = title
    description = "Чтение объявления из БД"
    on { state == MkplState.RUNNING }
    handle {
        val request = DbPublicationIdRequest(publicationValidated)
        when(val result = publicationRepo.readPublication(request)) {
            is DbPublicationResponseOk -> publicationRepoRead = result.data
            is DbPublicationResponseErr -> fail(result.errors)
            is DbPublicationResponseErrWithData -> {
                fail(result.errors)
                publicationRepoRead = result.data
            }
        }
    }
}
