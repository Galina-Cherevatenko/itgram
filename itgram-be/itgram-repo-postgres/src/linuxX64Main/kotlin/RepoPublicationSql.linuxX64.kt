package ru.itgram.backend.repo.postgresql

import io.github.moreirasantos.pgkn.PostgresDriver
import io.github.moreirasantos.pgkn.resultset.ResultSet
import kotlinx.coroutines.runBlocking
import ru.itgram.backend.repo.postgresql.SqlFields.quoted
import ru.itgram.common.models.MkplPublication
import ru.itgram.common.models.MkplPublicationId
import ru.itgram.common.models.MkplPublicationLock
import ru.itgram.common.models.MkplUserId
import ru.itgram.common.repo.*
import ru.itgram.repo.common.IRepoPublicationInitializable

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class RepoPublicationSql actual constructor(
    properties: SqlProperties,
    randomUuid: () -> String,
) : IRepoPublication, IRepoPublicationInitializable {
    actual override fun save(publications: Collection<MkplPublication>): Collection<MkplPublication> {
        TODO("Not yet implemented")
    }

    actual override suspend fun createPublication(rq: DbPublicationRequest): IDbPublicationResponse {
        TODO("Not yet implemented")
    }

    actual override suspend fun readPublication(rq: DbPublicationIdRequest): IDbPublicationResponse {
        TODO("Not yet implemented")
    }

    actual override suspend fun updatePublication(rq: DbPublicationRequest): IDbPublicationResponse {
        TODO("Not yet implemented")
    }

    actual override suspend fun deletePublication(rq: DbPublicationIdRequest): IDbPublicationResponse {
        TODO("Not yet implemented")
    }

    actual override suspend fun searchPublication(rq: DbPublicationFilterRequest): IDbPublicationsResponse {
        TODO("Not yet implemented")
    }

    actual fun clear() {
        TODO("Not yet implemented")
    }
}
