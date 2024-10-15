package ru.itgram.backend.repo.postgresql

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.toKString
import kotlinx.coroutines.test.runTest
import platform.posix.getenv
import ru.itgram.common.models.*
import ru.itgram.common.repo.DbPublicationFilterRequest
import ru.itgram.common.repo.DbPublicationRequest
import kotlin.test.Test

class RepoPublicationSqlTest {
    @OptIn(ExperimentalForeignApi::class)
    @Test
    fun create() = runTest {
        val pgPort = getenv("postgresPort")?.toKString()?.toIntOrNull() ?: 5432

        val repo = RepoPublicationSql(
            properties = SqlProperties(port = pgPort)
        )
        val res = repo.createPublication(
            rq = DbPublicationRequest(
                MkplPublication(
                    title = "tttt",
                    description = "zzzz",
                    visibility = MkplVisibility.VISIBLE_PUBLIC,
                    publicationCategory = MkplPublicationCategory.POST,
                    ownerId = MkplUserId("1234"),
                    lock = MkplPublicationLock("235356"),
                )
            )
        )
        println("CREATED $res")
    }

    @OptIn(ExperimentalForeignApi::class)
    @Test
    fun search() = runTest {
        val pgPort = getenv("postgresPort")?.toKString()?.toIntOrNull() ?: 5432

        val repo = RepoPublicationSql(
            properties = SqlProperties(port = pgPort)
        )
        val res = repo.searchPublication(
            rq = DbPublicationFilterRequest(
                titleFilter = "tttt",
                publicationCategory = MkplPublicationCategory.POST,
                ownerId = MkplUserId("1234"),
            )
        )
        println("SEARCH $res")
    }
}
