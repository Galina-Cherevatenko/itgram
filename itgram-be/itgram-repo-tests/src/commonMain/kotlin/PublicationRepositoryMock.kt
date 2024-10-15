package ru.itgram

import ru.itgram.common.models.MkplPublication
import ru.itgram.common.repo.*

class PublicationRepositoryMock(
    private val invokeCreatePublication: (DbPublicationRequest) -> IDbPublicationResponse = { DEFAULT_AD_SUCCESS_EMPTY_MOCK },
    private val invokeReadPublication: (DbPublicationIdRequest) -> IDbPublicationResponse = { DEFAULT_AD_SUCCESS_EMPTY_MOCK },
    private val invokeUpdatePublication: (DbPublicationRequest) -> IDbPublicationResponse = { DEFAULT_AD_SUCCESS_EMPTY_MOCK },
    private val invokeDeletePublication: (DbPublicationIdRequest) -> IDbPublicationResponse = { DEFAULT_AD_SUCCESS_EMPTY_MOCK },
    private val invokeSearchPublication: (DbPublicationFilterRequest) -> IDbPublicationsResponse = { DEFAULT_ADS_SUCCESS_EMPTY_MOCK },
): IRepoPublication {
    override suspend fun createPublication(rq: DbPublicationRequest): IDbPublicationResponse {
        return invokeCreatePublication(rq)
    }

    override suspend fun readPublication(rq: DbPublicationIdRequest): IDbPublicationResponse {
        return invokeReadPublication(rq)
    }

    override suspend fun updatePublication(rq: DbPublicationRequest): IDbPublicationResponse {
        return invokeUpdatePublication(rq)
    }

    override suspend fun deletePublication(rq: DbPublicationIdRequest): IDbPublicationResponse {
        return invokeDeletePublication(rq)
    }

    override suspend fun searchPublication(rq: DbPublicationFilterRequest): IDbPublicationsResponse {
        return invokeSearchPublication(rq)
    }

    companion object {
        val DEFAULT_AD_SUCCESS_EMPTY_MOCK = DbPublicationResponseOk(MkplPublication())
        val DEFAULT_ADS_SUCCESS_EMPTY_MOCK = DbPublicationsResponseOk(emptyList())
    }
}
