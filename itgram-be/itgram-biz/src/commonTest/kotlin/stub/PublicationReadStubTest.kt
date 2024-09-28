package ru.itgram.biz.stub

import kotlinx.coroutines.test.runTest
import ru.itgram.biz.MkplPublicationProcessor
import ru.itgram.common.MkplContext
import ru.itgram.common.models.*
import ru.itgram.common.stubs.MkplStubs
import kotlin.test.Test
import kotlin.test.assertEquals

class PublicationReadStubTest {

    private val processor = MkplPublicationProcessor()
    val id = MkplPublicationId("123")

    @Test
    fun read() = runTest {

        val ctx = MkplContext(
            command = MkplCommand.READ,
            state = MkplState.NONE,
            workMode = MkplWorkMode.STUB,
            stubCase = MkplStubs.SUCCESS,
            publicationRequest = MkplPublication(
                id = id,
            ),
        )
        processor.exec(ctx)
        with (MkplPublicationStub.get()) {
            assertEquals(id, ctx.publicationResponse.id)
            assertEquals(title, ctx.publicationResponse.title)
            assertEquals(description, ctx.publicationResponse.description)
            assertEquals(publicationCategory, ctx.publicationResponse.publicationCategory)
            assertEquals(visibility, ctx.publicationResponse.visibility)
        }
    }

    @Test
    fun badId() = runTest {
        val ctx = MkplContext(
            command = MkplCommand.READ,
            state = MkplState.NONE,
            workMode = MkplWorkMode.STUB,
            stubCase = MkplStubs.BAD_ID,
            publicationRequest = MkplPublication(),
        )
        processor.exec(ctx)
        assertEquals(MkplPublication(), ctx.publicationResponse)
        assertEquals("id", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun databaseError() = runTest {
        val ctx = MkplContext(
            command = MkplCommand.READ,
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
            command = MkplCommand.READ,
            state = MkplState.NONE,
            workMode = MkplWorkMode.STUB,
            stubCase = MkplStubs.BAD_TITLE,
            publicationRequest = MkplPublication(
                id = id,
            ),
        )
        processor.exec(ctx)
        assertEquals(MkplPublication(), ctx.publicationResponse)
        assertEquals("stub", ctx.errors.firstOrNull()?.field)
    }
}
