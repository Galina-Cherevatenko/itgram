package ru.itgram.repo.common

import ru.itgram.common.models.MkplPublication
import ru.itgram.common.repo.IRepoPublication

interface IRepoPublicationInitializable: IRepoPublication {
    fun save(pudblications: Collection<MkplPublication>): Collection<MkplPublication>
}