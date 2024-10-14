package ru.itgram

import ru.itgram.common.models.MkplPublication
import ru.itgram.common.models.MkplPublicationCategory
import ru.itgram.common.models.MkplUserId
import ru.itgram.common.repo.DbPublicationFilterRequest
import ru.itgram.common.repo.DbPublicationsResponseOk
import ru.itgram.common.repo.IRepoPublication
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

abstract class RepoPublicationSearchTest {
    abstract val repo: IRepoPublication

    protected open val initializedObjects: List<MkplPublication> = initObjects

    @Test
    fun searchPublicationCategory() = runRepoTest {
        val result = repo.searchPublication(DbPublicationFilterRequest(publicationCategory = MkplPublicationCategory.POST))
        assertIs<DbPublicationsResponseOk>(result)
        val expected = listOf(initializedObjects[0])
        assertEquals(expected, result.data.sortedBy { it.id.asString() })
    }

    companion object: BaseInitPublications("search") {

        val searchOwnerId = MkplUserId("owner-124")
        override val initObjects: List<MkplPublication> = listOf(
            createInitTestModel("publication1", publicationCategory = MkplPublicationCategory.POST),
            createInitTestModel("publication2", ownerId = searchOwnerId),
            createInitTestModel("publication3", publicationCategory = MkplPublicationCategory.START),
            createInitTestModel("publication4", ownerId = searchOwnerId),
            createInitTestModel("publication5", publicationCategory =  MkplPublicationCategory.START),
        )
    }
}
