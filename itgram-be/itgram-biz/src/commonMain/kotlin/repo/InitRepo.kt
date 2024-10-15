package ru.itgram.biz.repository

import ru.itgram.biz.exceptions.MkplPublicationDbNotConfiguredException
import ru.itgram.common.MkplContext
import ru.itgram.common.helpers.errorSystem
import ru.itgram.common.helpers.fail
import ru.itgram.common.models.MkplWorkMode
import ru.itgram.common.repo.IRepoPublication
import ru.itgram.cor.ICorChainDsl
import ru.itgram.cor.worker

fun ICorChainDsl<MkplContext>.initRepo(title: String) = worker {
    this.title = title
    description = """
        Вычисление основного рабочего репозитория в зависимости от зпрошенного режима работы        
    """.trimIndent()
    handle {
        publicationRepo = when {
            workMode == MkplWorkMode.TEST -> corSettings.repoTest
            workMode == MkplWorkMode.STUB -> corSettings.repoStub
            else -> corSettings.repoProd
        }
        if (workMode != MkplWorkMode.STUB && publicationRepo == IRepoPublication.NONE) fail(
            errorSystem(
                violationCode = "dbNotConfigured",
                e = MkplPublicationDbNotConfiguredException(workMode)
            )
        )
    }
}