package ru.itgram.common

import ru.itgram.common.repo.IRepoPublication
import ru.itgram.logging.common.MpLoggerProvider


data class MkplCorSettings(
    val loggerProvider: MpLoggerProvider = MpLoggerProvider(),
    val repoStub: IRepoPublication = IRepoPublication.NONE,
    val repoTest: IRepoPublication = IRepoPublication.NONE,
    val repoProd: IRepoPublication = IRepoPublication.NONE,
) {
    companion object {
        val NONE = MkplCorSettings()
    }
}
