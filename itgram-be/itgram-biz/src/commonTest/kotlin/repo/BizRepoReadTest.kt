package ru.itgram.biz.repo

import kotlinx.coroutines.test.runTest
import ru.itgram.PublicationRepositoryMock
import ru.itgram.biz.MkplPublicationProcessor
import ru.itgram.common.MkplContext
import ru.itgram.common.MkplCorSettings
import ru.itgram.common.models.*
import ru.itgram.common.repo.DbPublicationResponseOk
import kotlin.test.Test
import kotlin.test.assertEquals

class BizRepoReadTest {

    private val userId = MkplUserId("321")
    private val command = MkplCommand.READ
    private val initPublication = MkplPublication(
        id = MkplPublicationId("123"),
        title = "abc",
        description = "abc",
        ownerId = userId,
        publicationCategory = MkplPublicationCategory.START,
        visibility = MkplVisibility.VISIBLE_PUBLIC,
    )
    private val repo = PublicationRepositoryMock(
        invokeReadPublication = {
            DbPublicationResponseOk(
                data = initPublication,
            )
        }
    )
    private val settings = MkplCorSettings(repoTest = repo)
    private val processor = MkplPublicationProcessor(settings)

    @Test
    fun repoReadSuccessTest() = runTest {
        val ctx = MkplContext(
            command = command,
            state = MkplState.NONE,
            workMode = MkplWorkMode.TEST,
            publicationRequest = MkplPublication(
                id = MkplPublicationId("123"),
            ),
        )
        processor.exec(ctx)
        assertEquals(MkplState.FINISHING, ctx.state)
        assertEquals(initPublication.id, ctx.publicationResponse.id)
        assertEquals(initPublication.title, ctx.publicationResponse.title)
        assertEquals(initPublication.description, ctx.publicationResponse.description)
        assertEquals(initPublication.publicationCategory, ctx.publicationResponse.publicationCategory)
        assertEquals(initPublication.visibility, ctx.publicationResponse.visibility)
    }

    @Test
    fun repoReadNotFoundTest() = repoNotFoundTest(command)
}
