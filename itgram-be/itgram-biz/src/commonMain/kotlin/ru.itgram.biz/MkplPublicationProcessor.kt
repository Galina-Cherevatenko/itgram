package ru.itgram.biz

import ru.itgram.common.MkplContext
import ru.itgram.common.MkplCorSettings
import ru.itgram.common.models.MkplPublicationCategory
import ru.itgram.common.models.MkplState

@Suppress("unused", "RedundantSuspendModifier")
class MkplPublicationProcessor(val corSettings: MkplCorSettings) {
    suspend fun exec(ctx: MkplContext) {
        ctx.publicationResponse = MkplPublicationStub.get()
        ctx.publicationsResponse = MkplPublicationStub.prepareSearchList("publication search", MkplPublicationCategory.POST).toMutableList()
        ctx.state = MkplState.RUNNING
    }
}