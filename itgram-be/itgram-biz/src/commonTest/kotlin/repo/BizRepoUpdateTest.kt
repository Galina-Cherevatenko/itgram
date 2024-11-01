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

class BizRepoUpdateTest {

    private val userId = MkplUserId("321")
    private val command = MkplCommand.UPDATE
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
        invokeUpdatePublication = {
            DbPublicationResponseOk(
                data = MkplPublication(
                    id = MkplPublicationId("123"),
                    title = "xyz",
                    description = "xyz",
                    publicationCategory = MkplPublicationCategory.START,
                    visibility = MkplVisibility.VISIBLE_TO_GROUP,
                )
            )
        }
    )
    private val settings = MkplCorSettings(repoTest = repo)
    private val processor = MkplPublicationProcessor(settings)

    @Test
    fun repoUpdateSuccessTest() = runTest {
        val publicationToUpdate = MkplPublication(
            id = MkplPublicationId("123"),
            title = "xyz",
            description = "xyz",
            publicationCategory = MkplPublicationCategory.START,
            visibility = MkplVisibility.VISIBLE_TO_GROUP,
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
        assertEquals(publicationToUpdate.id, ctx.publicationResponse.id)
        assertEquals(publicationToUpdate.title, ctx.publicationResponse.title)
        assertEquals(publicationToUpdate.description, ctx.publicationResponse.description)
        assertEquals(publicationToUpdate.publicationCategory, ctx.publicationResponse.publicationCategory)
        assertEquals(publicationToUpdate.visibility, ctx.publicationResponse.visibility)
    }

    @Test
    fun repoUpdateNotFoundTest() = repoNotFoundTest(command)
}
