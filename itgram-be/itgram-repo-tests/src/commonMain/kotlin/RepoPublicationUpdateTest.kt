package ru.itgram

import ru.itgram.common.models.*
import ru.itgram.common.repo.DbPublicationRequest
import ru.itgram.common.repo.DbPublicationResponseErr
import ru.itgram.common.repo.DbPublicationResponseOk
import ru.itgram.common.repo.IRepoPublication
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

abstract class RepoPublicationUpdateTest {
    abstract val repo: IRepoPublication
    protected open val updateSucc = initObjects[0]
    protected val updateIdNotFound = MkplPublicationId("publication-repo-update-not-found")
    protected val lockBad = MkplPublicationLock("20000000-0000-0000-0000-000000000009")
    protected val lockNew = MkplPublicationLock("20000000-0000-0000-0000-000000000002")

    private val reqUpdateSucc by lazy {
        MkplPublication(
            id = updateSucc.id,
            title = "update object",
            description = "update object description",
            ownerId = MkplUserId("owner-123"),
            visibility = MkplVisibility.VISIBLE_TO_GROUP,
            publicationCategory = MkplPublicationCategory.START,
            lock = initObjects.first().lock,
        )
    }
    private val reqUpdateNotFound = MkplPublication(
        id = updateIdNotFound,
        title = "update object not found",
        description = "update object not found description",
        ownerId = MkplUserId("owner-123"),
        visibility = MkplVisibility.VISIBLE_TO_GROUP,
        publicationCategory = MkplPublicationCategory.START,
        lock = initObjects.first().lock
    )

    @Test
    fun updateSuccess() = runRepoTest {
        val result = repo.updatePublication(DbPublicationRequest(reqUpdateSucc))
        assertIs<DbPublicationResponseOk>(result)
        assertEquals(reqUpdateSucc.id, result.data.id)
        assertEquals(reqUpdateSucc.title, result.data.title)
        assertEquals(reqUpdateSucc.description, result.data.description)
        assertEquals(reqUpdateSucc.publicationCategory, result.data.publicationCategory)
        assertEquals(lockNew, result.data.lock)
    }

    @Test
    fun updateNotFound() = runRepoTest {
        val result = repo.updatePublication(DbPublicationRequest(reqUpdateNotFound))
        assertIs<DbPublicationResponseErr>(result)
        val error = result.errors.find { it.code == "repo-not-found" }
        assertEquals("id", error?.field)
    }

    companion object : BaseInitPublications("update") {
        override val initObjects: List<MkplPublication> = listOf(
            createInitTestModel("update"),
        )
    }
}
