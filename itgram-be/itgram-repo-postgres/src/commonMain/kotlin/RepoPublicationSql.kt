package ru.itgram.backend.repo.postgresql

import ru.itgram.repo.common.IRepoPublicationInitializable
import com.benasher44.uuid.uuid4
import ru.itgram.common.models.MkplPublication
import ru.itgram.common.repo.*

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect class RepoPublicationSql(
    properties: SqlProperties,
    randomUuid: () -> String = { uuid4().toString() },
) : IRepoPublication, IRepoPublicationInitializable {
    override fun save(publications: Collection<MkplPublication>): Collection<MkplPublication>
    override suspend fun createPublication(rq: DbPublicationRequest): IDbPublicationResponse
    override suspend fun readPublication(rq: DbPublicationIdRequest): IDbPublicationResponse
    override suspend fun updatePublication(rq: DbPublicationRequest): IDbPublicationResponse
    override suspend fun deletePublication(rq: DbPublicationIdRequest): IDbPublicationResponse
    override suspend fun searchPublication(rq: DbPublicationFilterRequest): IDbPublicationsResponse
    fun clear()
}