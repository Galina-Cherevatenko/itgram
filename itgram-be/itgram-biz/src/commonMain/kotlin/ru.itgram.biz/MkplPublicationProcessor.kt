package ru.itgram.biz
@Suppress("unused", "RedundantSuspendModifier")
class MkplPublicationProcessor(val corSettings: MkplCorSettings) {
    suspend fun exec(ctx: MkplContext) {
        ctx.adResponse = MkplPublicationStub.get()
        ctx.adsResponse = MkplPublicationStub.prepareSearchList("ad search", MkplDealSide.DEMAND).toMutableList()
        ctx.state = MkplState.RUNNING
    }
}