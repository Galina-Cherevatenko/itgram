package ru.itgram.biz.repository

import ru.itgram.common.MkplContext
import ru.itgram.common.helpers.fail
import ru.itgram.common.models.MkplState
import ru.itgram.common.repo.DbPublicationFilterRequest
import ru.itgram.common.repo.DbPublicationsResponseErr
import ru.itgram.common.repo.DbPublicationsResponseOk
import ru.itgram.cor.ICorChainDsl
import ru.itgram.cor.worker

fun ICorChainDsl<MkplContext>.repoSearch(title: String) = worker {
    this.title = title
    description = "Поиск публикаций в БД по фильтру"
    on { state == MkplState.RUNNING }
    handle {
        val request = DbPublicationFilterRequest(
            titleFilter = publicationFilterValidated.searchString,
            ownerId = publicationFilterValidated.ownerId,
            publicationCategory = publicationFilterValidated.publicationCategory,
        )
        when(val result = publicationRepo.searchPublication(request)) {
            is DbPublicationsResponseOk -> publicationsRepoDone = result.data.toMutableList()
            is DbPublicationsResponseErr -> fail(result.errors)
        }
    }
}