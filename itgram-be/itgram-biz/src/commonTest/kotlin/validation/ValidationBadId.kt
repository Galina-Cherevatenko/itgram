package ru.itgram.biz.validation

import ru.itgram.biz.MkplPublicationProcessor
import kotlinx.coroutines.test.runTest
import ru.itgram.common.MkplContext
import ru.itgram.common.models.*
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

fun validationIdCorrect(command: MkplCommand, processor: MkplPublicationProcessor) = runTest {
    val ctx = MkplContext(
        command = command,
        state = MkplState.NONE,
        workMode = MkplWorkMode.TEST,
        publicationRequest = MkplPublicationStub.get(),
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(MkplState.FAILING, ctx.state)
}

fun validationIdTrim(command: MkplCommand, processor: MkplPublicationProcessor) = runTest {
    val ctx = MkplContext(
        command = command,
        state = MkplState.NONE,
        workMode = MkplWorkMode.TEST,
        publicationRequest = MkplPublicationStub.prepareResult {
            id = MkplPublicationId(" \n\t ${id.asString()} \n\t ")
        },
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(MkplState.FAILING, ctx.state)
}

fun validationIdEmpty(command: MkplCommand, processor: MkplPublicationProcessor) = runTest {
    val ctx = MkplContext(
        command = command,
        state = MkplState.NONE,
        workMode = MkplWorkMode.TEST,
        publicationRequest = MkplPublication(
            id = MkplPublicationId(""),
            title = "abc",
            description = "abc",
            publicationCategory = MkplPublicationCategory.POST,
            visibility = MkplVisibility.VISIBLE_PUBLIC,
            lock = MkplPublicationLock("123-234-abc-ABC"),
        ),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(MkplState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("id", error?.field)
    assertContains(error?.message ?: "", "id")
}

fun validationIdFormat(command: MkplCommand, processor: MkplPublicationProcessor) = runTest {
    val ctx = MkplContext(
        command = command,
        state = MkplState.NONE,
        workMode = MkplWorkMode.TEST,
        publicationRequest = MkplPublication(
            id = MkplPublicationId("!@#\$%^&*(),.{}"),
            title = "abc",
            description = "abc",
            publicationCategory = MkplPublicationCategory.POST,
            visibility = MkplVisibility.VISIBLE_PUBLIC,
            lock = MkplPublicationLock("123-234-abc-ABC"),
        ),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(MkplState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("id", error?.field)
    assertContains(error?.message ?: "", "id")
}
