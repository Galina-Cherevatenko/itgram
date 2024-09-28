package ru.itgram.biz.stub

import MkplPublicationProcessor
import kotlinx.coroutines.test.runTest
import ru.itgram.common.MkplContext
import ru.itgram.common.models.*
import ru.itgram.common.stubs.MkplStubs
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.fail

class PublicationSearchStubTest {

    private val processor = MkplPublicationProcessor()
    val filter = MkplPublicationFilter(searchString = "bolt")

    @Test
    fun read() = runTest {

        val ctx = MkplContext(
            command = MkplCommand.SEARCH,
            state = MkplState.NONE,
            workMode = MkplWorkMode.STUB,
            stubCase = MkplStubs.SUCCESS,
            publicationFilterRequest = filter,
        )
        processor.exec(ctx)
        assertTrue(ctx.publicationsResponse.size > 1)
        val first = ctx.publicationsResponse.firstOrNull() ?: fail("Empty response list")
        assertTrue(first.title.contains(filter.searchString))
        assertTrue(first.description.contains(filter.searchString))
        with (MkplPublicationStub.get()) {
            assertEquals(publicationCategory, first.publicationCategory)
            assertEquals(visibility, first.visibility)
        }
    }

    @Test
    fun badId() = runTest {
        val ctx = MkplContext(
            command = MkplCommand.SEARCH,
            state = MkplState.NONE,
            workMode = MkplWorkMode.STUB,
            stubCase = MkplStubs.BAD_ID,
            publicationFilterRequest = filter,
        )
        processor.exec(ctx)
        assertEquals(MkplPublication(), ctx.publicationResponse)
        assertEquals("id", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun databaseError() = runTest {
        val ctx = MkplContext(
            command = MkplCommand.SEARCH,
            state = MkplState.NONE,
            workMode = MkplWorkMode.STUB,
            stubCase = MkplStubs.DB_ERROR,
            publicationFilterRequest = filter,
        )
        processor.exec(ctx)
        assertEquals(MkplPublication(), ctx.publicationResponse)
        assertEquals("internal", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun badNoCase() = runTest {
        val ctx = MkplContext(
            command = MkplCommand.SEARCH,
            state = MkplState.NONE,
            workMode = MkplWorkMode.STUB,
            stubCase = MkplStubs.BAD_TITLE,
            publicationFilterRequest = filter,
        )
        processor.exec(ctx)
        assertEquals(MkplPublication(), ctx.publicationResponse)
        assertEquals("stub", ctx.errors.firstOrNull()?.field)
    }
}
