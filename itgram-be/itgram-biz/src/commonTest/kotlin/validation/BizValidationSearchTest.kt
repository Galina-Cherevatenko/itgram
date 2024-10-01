package ru.itgram.biz.validation

import kotlinx.coroutines.test.runTest
import ru.itgram.common.MkplContext
import ru.itgram.common.models.MkplCommand
import ru.itgram.common.models.MkplPublicationFilter
import ru.itgram.common.models.MkplState
import ru.itgram.common.models.MkplWorkMode
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class BizValidationSearchTest: BaseBizValidationTest() {
    override val command = MkplCommand.SEARCH

    @Test
    fun correctEmpty() = runTest {
        val ctx = MkplContext(
            command = command,
            state = MkplState.NONE,
            workMode = MkplWorkMode.TEST,
            publicationFilterRequest = MkplPublicationFilter()
        )
        processor.exec(ctx)
        assertEquals(0, ctx.errors.size)
        assertNotEquals(MkplState.FAILING, ctx.state)
    }
}