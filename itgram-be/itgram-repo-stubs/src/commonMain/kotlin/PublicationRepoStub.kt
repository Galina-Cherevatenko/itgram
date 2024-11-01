import ru.itgram.common.models.MkplPublicationCategory
import ru.itgram.common.repo.*

class PublicationRepoStub() : IRepoPublication {
    override suspend fun createPublication(rq: DbPublicationRequest): IDbPublicationResponse {
        return DbPublicationResponseOk(
            data = MkplPublicationStub.get(),
        )
    }

    override suspend fun readPublication(rq: DbPublicationIdRequest): IDbPublicationResponse {
        return DbPublicationResponseOk(
            data = MkplPublicationStub.get(),
        )
    }

    override suspend fun updatePublication(rq: DbPublicationRequest): IDbPublicationResponse {
        return DbPublicationResponseOk(
            data = MkplPublicationStub.get(),
        )
    }

    override suspend fun deletePublication(rq: DbPublicationIdRequest): IDbPublicationResponse {
        return DbPublicationResponseOk(
            data = MkplPublicationStub.get(),
        )
    }

    override suspend fun searchPublication(rq: DbPublicationFilterRequest): IDbPublicationsResponse {
        return DbPublicationsResponseOk(
            data = MkplPublicationStub.prepareSearchList(filter = "", MkplPublicationCategory.POST),
        )
    }
}