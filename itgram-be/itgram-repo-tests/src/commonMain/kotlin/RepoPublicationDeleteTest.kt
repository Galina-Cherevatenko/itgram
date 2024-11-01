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
import kotlin.test.assertNotNull

abstract class RepoPublicationDeleteTest {
    abstract val repo: IRepoPublication
    protected open val deleteSucc = initObjects[0]
    protected open val notFoundId = MkplPublicationId("ad-repo-delete-notFound")

    @Test
    fun deleteSuccess() = runRepoTest {
        val lockOld = deleteSucc.lock
        val result = repo.deletePublication(DbPublicationIdRequest(deleteSucc.id, lock = lockOld))
        assertIs<DbPublicationResponseOk>(result)
        assertEquals(deleteSucc.title, result.data.title)
        assertEquals(deleteSucc.description, result.data.description)
    }

    @Test
    fun deleteNotFound() = runRepoTest {
        val result = repo.readPublication(DbPublicationIdRequest(notFoundId, lock = lockOld))

        assertIs<DbPublicationResponseErr>(result)
        val error = result.errors.find { it.code == "repo-not-found" }
        assertNotNull(error)
    }

    companion object : BaseInitPublications("delete") {
        override val initObjects: List<MkplPublication> = listOf(
            createInitTestModel("delete"),
            createInitTestModel("deleteLock"),
        )
    }
}

