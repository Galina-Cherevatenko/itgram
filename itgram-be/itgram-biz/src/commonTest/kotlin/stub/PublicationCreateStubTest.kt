package ru.itgram.biz.stub

import ru.itgram.biz.MkplPublicationProcessor
import kotlinx.coroutines.test.runTest
import ru.itgram.common.MkplContext
import ru.itgram.common.models.*
import ru.itgram.common.stubs.MkplStubs
import kotlin.test.Test
import kotlin.test.assertEquals

class PublicationCreateStubTest {

    private val processor = MkplPublicationProcessor()
    val id = MkplPublicationId("123")
    val title = "my new work"
    val description = "рад вас сообщить о начале новой работы"
    val category = MkplPublicationCategory.POST
    val visibility = MkplVisibility.VISIBLE_PUBLIC

    @Test
    fun create() = runTest {

        val ctx = MkplContext(
            command = MkplCommand.CREATE,
            state = MkplState.NONE,
            workMode = MkplWorkMode.STUB,
            stubCase = MkplStubs.SUCCESS,
            publicationRequest = MkplPublication(
                id = id,
                title = title,
                description = description,
                publicationCategory = category,
                visibility = visibility,
            ),
        )
        processor.exec(ctx)
        assertEquals(MkplPublicationStub.get().id, ctx.publicationResponse.id)
        assertEquals(title, ctx.publicationResponse.title)
        assertEquals(description, ctx.publicationResponse.description)
        assertEquals(category, ctx.publicationResponse.publicationCategory)
        assertEquals(visibility, ctx.publicationResponse.visibility)
    }

    @Test
    fun badTitle() = runTest {
        val ctx = MkplContext(
            command = MkplCommand.CREATE,
            state = MkplState.NONE,
            workMode = MkplWorkMode.STUB,
            stubCase = MkplStubs.BAD_TITLE,
            publicationRequest = MkplPublication(
                id = id,
                title = "",
                description = description,
                publicationCategory = category,
                visibility = visibility,
            ),
        )
        processor.exec(ctx)
        assertEquals(MkplPublication(), ctx.publicationResponse)
        assertEquals("title", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }
    @Test
    fun badDescription() = runTest {
        val ctx = MkplContext(
            command = MkplCommand.CREATE,
            state = MkplState.NONE,
            workMode = MkplWorkMode.STUB,
            stubCase = MkplStubs.BAD_DESCRIPTION,
            publicationRequest = MkplPublication(
                id = id,
                title = title,
                description = "",
                publicationCategory = category,
                visibility = visibility,
            ),
        )
        processor.exec(ctx)
        assertEquals(MkplPublication(), ctx.publicationResponse)
        assertEquals("description", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun databaseError() = runTest {
        val ctx = MkplContext(
            command = MkplCommand.CREATE,
            state = MkplState.NONE,
            workMode = MkplWorkMode.STUB,
            stubCase = MkplStubs.DB_ERROR,
            publicationRequest = MkplPublication(
                id = id,
            ),
        )
        processor.exec(ctx)
        assertEquals(MkplPublication(), ctx.publicationResponse)
        assertEquals("internal", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun badNoCase() = runTest {
        val ctx = MkplContext(
            command = MkplCommand.CREATE,
            state = MkplState.NONE,
            workMode = MkplWorkMode.STUB,
            stubCase = MkplStubs.BAD_ID,
            publicationRequest = MkplPublication(
                id = id,
                title = title,
                description = description,
                publicationCategory = category,
                visibility = visibility,
            ),
        )
        processor.exec(ctx)
        assertEquals(MkplPublication(), ctx.publicationResponse)
        assertEquals("stub", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }
}
