package ru.itgram

import IRepoPublicationInitializable
import ru.itgram.common.models.*
import ru.itgram.common.repo.DbPublicationRequest
import ru.itgram.common.repo.DbPublicationResponseOk
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotEquals

abstract class RepoPublicationCreateTest {
    abstract val repo: IRepoPublicationInitializable
    protected open val uuidNew = MkplPublicationId("10000000-0000-0000-0000-000000000001")

    private val createObj = MkplPublication(
        title = "create object",
        description = "create object description",
        ownerId = MkplUserId("owner-123"),
        visibility = MkplVisibility.VISIBLE_TO_GROUP,
        publicationCategory = MkplPublicationCategory.START,
    )

    @Test
    fun createSuccess() = runRepoTest {
        val result = repo.createPublication(DbPublicationRequest(createObj))
        val expected = createObj
        assertIs<DbPublicationResponseOk>(result)
        assertEquals(uuidNew, result.data.id)
        assertEquals(expected.title, result.data.title)
        assertEquals(expected.description, result.data.description)
        assertEquals(expected.publicationCategory, result.data.publicationCategory)
        assertNotEquals(MkplPublicationId.NONE, result.data.id)
    }

    companion object : BaseInitPublications("create") {
        override val initObjects: List<MkplPublication> = emptyList()
    }
}
