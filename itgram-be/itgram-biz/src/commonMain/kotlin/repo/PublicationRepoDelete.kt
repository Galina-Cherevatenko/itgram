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

fun ICorChainDsl<MkplContext>.repoDelete(title: String) = worker {
    this.title = title
    description = "Удаление публкиации из БД по ID"
    on { state == MkplState.RUNNING }
    handle {
        val request = DbPublicationIdRequest(publicationRepoPrepare)
        when(val result = publicationRepo.deletePublication(request)) {
            is DbPublicationResponseOk -> publicationRepoDone = result.data
            is DbPublicationResponseErr -> {
                fail(result.errors)
                publicationRepoDone = publicationRepoRead
            }
            is DbPublicationResponseErrWithData -> {
                fail(result.errors)
                publicationRepoDone = result.data
            }
        }
    }
}