package ru.itgram.biz.repo

import kotlinx.coroutines.test.runTest
import ru.itgram.PublicationRepositoryMock
import ru.itgram.biz.MkplPublicationProcessor
import ru.itgram.common.MkplContext
import ru.itgram.common.MkplCorSettings
import ru.itgram.common.models.*
import ru.itgram.common.repo.DbPublicationsResponseOk
import kotlin.test.Test
import kotlin.test.assertEquals

class BizRepoSearchTest {

    private val userId = MkplUserId("321")
    private val command = MkplCommand.SEARCH
    private val initPublication = MkplPublication(
        id = MkplPublicationId("123"),
        title = "abc",
        description = "abc",
        ownerId = userId,
        publicationCategory = MkplPublicationCategory.START,
        visibility = MkplVisibility.VISIBLE_PUBLIC,
    )
    private val repo = PublicationRepositoryMock(
        invokeSearchPublication = {
            DbPublicationsResponseOk(
                data = listOf(initPublication),
            )
        }
    )
    private val settings = MkplCorSettings(repoTest = repo)
    private val processor = MkplPublicationProcessor(settings)

    @Test
    fun repoSearchSuccessTest() = runTest {
        val ctx = MkplContext(
            command = command,
            state = MkplState.NONE,
            workMode = MkplWorkMode.TEST,
            publicationFilterRequest = MkplPublicationFilter(
                searchString = "abc",
                publicationCategory = MkplPublicationCategory.START
            ),
        )
        processor.exec(ctx)
        assertEquals(MkplState.FINISHING, ctx.state)
        assertEquals(1, ctx.publicationsResponse.size)
    }
}
