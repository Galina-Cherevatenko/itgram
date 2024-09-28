package ru.itgram.biz.validation

import ru.itgram.biz.MkplPublicationProcessor
import kotlinx.coroutines.test.runTest
import ru.itgram.common.MkplContext
import ru.itgram.common.models.*
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

private val stub = MkplPublicationStub.get()

fun validationTitleCorrect(command: MkplCommand, processor: MkplPublicationProcessor) = runTest {
    val ctx = MkplContext(
        command = command,
        state = MkplState.NONE,
        workMode = MkplWorkMode.TEST,
        publicationRequest = MkplPublication(
            id = stub.id,
            title = "abc",
            description = "abc",
            publicationCategory = MkplPublicationCategory.POST,
            visibility = MkplVisibility.VISIBLE_PUBLIC,
            lock = MkplPublicationLock("123-234-abc-ABC"),
        ),
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(MkplState.FAILING, ctx.state)
    assertEquals("abc", ctx.publicationValidated.title)
}

fun validationTitleTrim(command: MkplCommand, processor: MkplPublicationProcessor) = runTest {
    val ctx = MkplContext(
        command = command,
        state = MkplState.NONE,
        workMode = MkplWorkMode.TEST,
        publicationRequest = MkplPublication(
            id = stub.id,
            title = " \n\t abc \t\n ",
            description = "abc",
            publicationCategory = MkplPublicationCategory.POST,
            visibility = MkplVisibility.VISIBLE_PUBLIC,
            lock = MkplPublicationLock("123-234-abc-ABC"),
        ),
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(MkplState.FAILING, ctx.state)
    assertEquals("abc", ctx.publicationValidated.title)
}

fun validationTitleEmpty(command: MkplCommand, processor: MkplPublicationProcessor) = runTest {
    val ctx = MkplContext(
        command = command,
        state = MkplState.NONE,
        workMode = MkplWorkMode.TEST,
        publicationRequest = MkplPublication(
            id = stub.id,
            title = "",
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
    assertEquals("title", error?.field)
    assertContains(error?.message ?: "", "title")
}

fun validationTitleSymbols(command: MkplCommand, processor: MkplPublicationProcessor) = runTest {
    val ctx = MkplContext(
        command = command,
        state = MkplState.NONE,
        workMode = MkplWorkMode.TEST,
        publicationRequest = MkplPublication(
            id = MkplPublicationId("123"),
            title = "!@#$%^&*(),.{}",
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
    assertEquals("title", error?.field)
    assertContains(error?.message ?: "", "title")
}
