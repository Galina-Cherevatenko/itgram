package ru.itgram

import ru.itgram.common.models.MkplPublication
import ru.itgram.common.models.MkplPublicationId
import ru.itgram.common.repo.DbPublicationIdRequest
import ru.itgram.common.repo.DbPublicationResponseErr
import ru.itgram.common.repo.DbPublicationResponseOk
import ru.itgram.common.repo.IRepoPublication
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

abstract class RepoPublicationReadTest {
    abstract val repo: IRepoPublication
    protected open val readSucc = initObjects[0]

    @Test
    fun readSuccess() = runRepoTest {
        val result = repo.readPublication(DbPublicationIdRequest(readSucc.id))

        assertIs<DbPublicationResponseOk>(result)
        assertEquals(readSucc, result.data)
    }

    @Test
    fun readNotFound() = runRepoTest {
        val result = repo.readPublication(DbPublicationIdRequest(notFoundId))

        assertIs<DbPublicationResponseErr>(result)
        val error = result.errors.find { it.code == "repo-not-found" }
        assertEquals("id", error?.field)
    }

    companion object : BaseInitPublications("delete") {
        override val initObjects: List<MkplPublication> = listOf(
            createInitTestModel("read")
        )

        val notFoundId = MkplPublicationId("publication-repo-read-notFound")

    }
}
