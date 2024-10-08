import ru.itgram.common.models.MkplPublication

class PublicationRepoInitialized(
    private val repo: IRepoPublicationInitializable,
    initObjects: Collection<MkplPublication> = emptyList(),
) : IRepoPublicationInitializable by repo {
    @Suppress("unused")
    val initializedObjects: List<MkplPublication> = save(initObjects).toList()
}