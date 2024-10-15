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
import kotlin.test.assertNotEquals

class BizRepoCreateTest {

    private val userId = MkplUserId("321")
    private val command = MkplCommand.CREATE
    private val uuid = "10000000-0000-0000-0000-000000000001"
    private val repo = PublicationRepositoryMock(
        invokeCreatePublication = {
            DbPublicationResponseOk(
                data = MkplPublication(
                    id = MkplPublicationId(uuid),
                    title = it.publication.title,
                    description = it.publication.description,
                    ownerId = userId,
                    publicationCategory = it.publication.publicationCategory,
                    visibility = it.publication.visibility,
                )
            )
        }
    )
    private val settings = MkplCorSettings(
        repoTest = repo
    )
    private val processor = MkplPublicationProcessor(settings)

    @Test
    fun repoCreateSuccessTest() = runTest {
        val ctx = MkplContext(
            command = command,
            state = MkplState.NONE,
            workMode = MkplWorkMode.TEST,
            publicationRequest = MkplPublication(
                title = "abc",
                description = "abc",
                publicationCategory = MkplPublicationCategory.START,
                visibility = MkplVisibility.VISIBLE_PUBLIC,
            ),
        )
        processor.exec(ctx)
        assertEquals(MkplState.FINISHING, ctx.state)
        assertNotEquals(MkplPublicationId.NONE, ctx.publicationResponse.id)
        assertEquals("abc", ctx.publicationResponse.title)
        assertEquals("abc", ctx.publicationResponse.description)
        assertEquals(MkplPublicationCategory.START, ctx.publicationResponse.publicationCategory)
        assertEquals(MkplVisibility.VISIBLE_PUBLIC, ctx.publicationResponse.visibility)
    }
}
