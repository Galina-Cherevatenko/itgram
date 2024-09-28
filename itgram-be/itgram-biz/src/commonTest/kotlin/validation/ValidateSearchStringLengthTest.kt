package ru.itgram.biz.validation

import kotlinx.coroutines.test.runTest
import ru.itgram.common.MkplContext
import ru.itgram.common.models.MkplPublicationFilter
import ru.itgram.common.models.MkplState
import ru.itgram.cor.rootChain
import kotlin.test.Test
import kotlin.test.assertEquals

class ValidateSearchStringLengthTest {
    @Test
    fun emptyString() = runTest {
        val ctx = MkplContext(state = MkplState.RUNNING, publicationFilterValidating = MkplPublicationFilter(searchString = ""))
        chain.exec(ctx)
        assertEquals(MkplState.RUNNING, ctx.state)
        assertEquals(0, ctx.errors.size)
    }

    @Test
    fun blankString() = runTest {
        val ctx = MkplContext(state = MkplState.RUNNING, publicationFilterValidating = MkplPublicationFilter(searchString = "  "))
        chain.exec(ctx)
        assertEquals(MkplState.RUNNING, ctx.state)
        assertEquals(0, ctx.errors.size)
    }

    @Test
    fun shortString() = runTest {
        val ctx = MkplContext(state = MkplState.RUNNING, publicationFilterValidating = MkplPublicationFilter(searchString = "12"))
        chain.exec(ctx)
        assertEquals(MkplState.FAILING, ctx.state)
        assertEquals(1, ctx.errors.size)
        assertEquals("validation-searchString-tooShort", ctx.errors.first().code)
    }

    @Test
    fun normalString() = runTest {
        val ctx = MkplContext(state = MkplState.RUNNING, publicationFilterValidating = MkplPublicationFilter(searchString = "123"))
        chain.exec(ctx)
        assertEquals(MkplState.RUNNING, ctx.state)
        assertEquals(0, ctx.errors.size)
    }

    @Test
    fun longString() = runTest {
        val ctx = MkplContext(state = MkplState.RUNNING, publicationFilterValidating = MkplPublicationFilter(searchString = "12".repeat(51)))
        chain.exec(ctx)
        assertEquals(MkplState.FAILING, ctx.state)
        assertEquals(1, ctx.errors.size)
        assertEquals("validation-searchString-tooLong", ctx.errors.first().code)
    }

    companion object {
        val chain = rootChain {
            validateSearchStringLength("")
        }.build()
    }
}
