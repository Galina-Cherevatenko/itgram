package ru.itgram.biz.repo

import kotlinx.coroutines.test.runTest
import ru.itgram.PublicationRepositoryMock
import ru.itgram.biz.MkplPublicationProcessor
import ru.itgram.common.MkplContext
import ru.itgram.common.MkplCorSettings
import ru.itgram.common.models.*
import ru.itgram.common.repo.DbPublicationResponseOk
import ru.itgram.common.repo.errorNotFound
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

private val initPublication = MkplPublication(
    id = MkplPublicationId("123"),
    title = "nice title",
    description = "good description",
    publicationCategory = MkplPublicationCategory.START,
    visibility = MkplVisibility.VISIBLE_PUBLIC,
)
private val repo = PublicationRepositoryMock(
    invokeReadPublication = {
        if (it.id == initPublication.id) {
            DbPublicationResponseOk(
                data = initPublication,
            )
        } else errorNotFound(it.id)
    }
)
private val settings = MkplCorSettings(repoTest = repo)
private val processor = MkplPublicationProcessor(settings)

fun repoNotFoundTest(command: MkplCommand) = runTest {
    val ctx = MkplContext(
        command = command,
        state = MkplState.NONE,
        workMode = MkplWorkMode.TEST,
        publicationRequest = MkplPublication(
            id = MkplPublicationId("12345"),
            title = "xyz",
            description = "xyz",
            publicationCategory = MkplPublicationCategory.START,
            visibility = MkplVisibility.VISIBLE_TO_GROUP,
            lock = MkplPublicationLock("123"),
        ),
    )
    processor.exec(ctx)
    assertEquals(MkplState.FAILING, ctx.state)
    assertEquals(MkplPublication(), ctx.publicationResponse)
    assertEquals(1, ctx.errors.size)
    assertNotNull(ctx.errors.find { it.code == "repo-not-found" }, "Errors must contain not-found")
}
