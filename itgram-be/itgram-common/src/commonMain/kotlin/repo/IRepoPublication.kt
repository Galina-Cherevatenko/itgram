package ru.itgram.common.repo

interface IRepoPublication {

    suspend fun createPublication(rq: DbPublicationRequest): IDbPublicationResponse
    suspend fun readPublication(rq: DbPublicationIdRequest): IDbPublicationResponse
    suspend fun updatePublication(rq: DbPublicationRequest): IDbPublicationResponse
    suspend fun deletePublication(rq: DbPublicationIdRequest): IDbPublicationResponse
    suspend fun searchPublication(rq: DbPublicationFilterRequest): IDbPublicationsResponse

    companion object {
        val NONE = object : IRepoPublication {
            override suspend fun createPublication(rq: DbPublicationRequest): IDbPublicationResponse {
                throw NotImplementedError("Must not be used")
            }

            override suspend fun readPublication(rq: DbPublicationIdRequest): IDbPublicationResponse {
                throw NotImplementedError("Must not be used")
            }

            override suspend fun updatePublication(rq: DbPublicationRequest): IDbPublicationResponse {
                throw NotImplementedError("Must not be used")
            }

            override suspend fun deletePublication(rq: DbPublicationIdRequest): IDbPublicationResponse {
                throw NotImplementedError("Must not be used")
            }

            override suspend fun searchPublication(rq: DbPublicationFilterRequest): IDbPublicationsResponse {
                throw NotImplementedError("Must not be used")
            }
        }
    }
}
