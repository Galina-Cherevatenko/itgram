package ru.itgram.biz.validation

import kotlinx.coroutines.test.runTest
import ru.itgram.common.MkplContext
import ru.itgram.common.models.MkplPublication
import ru.itgram.common.models.MkplPublicationFilter
import ru.itgram.common.models.MkplState
import ru.itgram.cor.rootChain
import kotlin.test.Test
import kotlin.test.assertEquals

class ValidateTitleHasContentTest {
    @Test
    fun emptyString() = runTest {
        val ctx = MkplContext(state = MkplState.RUNNING, publicationValidating = MkplPublication(title = ""))
        chain.exec(ctx)
        assertEquals(MkplState.RUNNING, ctx.state)
        assertEquals(0, ctx.errors.size)
    }

    @Test
    fun noContent() = runTest {
        val ctx = MkplContext(state = MkplState.RUNNING, publicationValidating = MkplPublication(title = "12!@#$%^&*()_+-="))
        chain.exec(ctx)
        assertEquals(MkplState.FAILING, ctx.state)
        assertEquals(1, ctx.errors.size)
        assertEquals("validation-title-noContent", ctx.errors.first().code)
    }

    @Test
    fun normalString() = runTest {
        val ctx = MkplContext(state = MkplState.RUNNING, publicationFilterValidating = MkplPublicationFilter(searchString = "Ж"))
        chain.exec(ctx)
        assertEquals(MkplState.RUNNING, ctx.state)
        assertEquals(0, ctx.errors.size)
    }

    companion object {
        val chain = rootChain {
            validateTitleHasContent("")
        }.build()
    }
}
