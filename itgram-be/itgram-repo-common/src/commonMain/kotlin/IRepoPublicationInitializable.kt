import ru.itgram.common.models.MkplPublication
import ru.itgram.common.repo.IRepoPublication

interface IRepoPublicationInitializable: IRepoPublication {
    fun save(ads: Collection<MkplPublication>): Collection<MkplPublication>
}