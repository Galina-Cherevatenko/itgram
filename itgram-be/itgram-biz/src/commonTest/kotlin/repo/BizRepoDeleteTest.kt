package ru.itgram.biz.repo

import kotlinx.coroutines.test.runTest
import ru.itgram.PublicationRepositoryMock
import ru.itgram.biz.MkplPublicationProcessor
import ru.itgram.common.MkplContext
import ru.itgram.common.MkplCorSettings
import ru.itgram.common.models.*
import ru.itgram.common.repo.DbPublicationResponseErr
import ru.itgram.common.repo.DbPublicationResponseOk
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class BizRepoDeleteTest {

    private val userId = MkplUserId("321")
    private val command = MkplCommand.DELETE
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
        },
        invokeDeletePublication = {
            if (it.id == initPublication.id)
                DbPublicationResponseOk(
                    data = initPublication
                )
            else DbPublicationResponseErr()
        }
    )
    private val settings by lazy {
        MkplCorSettings(
            repoTest = repo
        )
    }
    private val processor = MkplPublicationProcessor(settings)

    @Test
    fun repoDeleteSuccessTest() = runTest {
        val publicationToUpdate = MkplPublication(
            id = MkplPublicationId("123"),
            lock = MkplPublicationLock("123"),
        )
        val ctx = MkplContext(
            command = command,
            state = MkplState.NONE,
            workMode = MkplWorkMode.TEST,
            publicationRequest = publicationToUpdate,
        )
        processor.exec(ctx)
        assertEquals(MkplState.FINISHING, ctx.state)
        assertTrue { ctx.errors.isEmpty() }
        assertEquals(initPublication.id, ctx.publicationResponse.id)
        assertEquals(initPublication.title, ctx.publicationResponse.title)
        assertEquals(initPublication.description, ctx.publicationResponse.description)
        assertEquals(initPublication.publicationCategory, ctx.publicationResponse.publicationCategory)
        assertEquals(initPublication.visibility, ctx.publicationResponse.visibility)
    }

    @Test
    fun repoDeleteNotFoundTest() = repoNotFoundTest(command)
}
