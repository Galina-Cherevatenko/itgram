package ru.itgram.biz.validation

import MkplPublicationProcessor
import kotlinx.coroutines.test.runTest
import ru.itgram.common.MkplContext
import ru.itgram.common.models.*
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

fun validationLockCorrect(command: MkplCommand, processor: MkplPublicationProcessor) = runTest {
    val ctx = MkplContext(
        command = command,
        state = MkplState.NONE,
        workMode = MkplWorkMode.TEST,
        publicationRequest = MkplPublication(
            id = MkplPublicationId("123-234-abc-ABC"),
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
}

fun validationLockTrim(command: MkplCommand, processor: MkplPublicationProcessor) = runTest {
    val ctx = MkplContext(
        command = command,
        state = MkplState.NONE,
        workMode = MkplWorkMode.TEST,
        publicationRequest = MkplPublication(
            id = MkplPublicationId("123-234-abc-ABC"),
            title = "abc",
            description = "abc",
            publicationCategory = MkplPublicationCategory.POST,
            visibility = MkplVisibility.VISIBLE_PUBLIC,
            lock = MkplPublicationLock(" \n\t 123-234-abc-ABC \n\t "),
        ),
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(MkplState.FAILING, ctx.state)
}

fun validationLockEmpty(command: MkplCommand, processor: MkplPublicationProcessor) = runTest {
    val ctx = MkplContext(
        command = command,
        state = MkplState.NONE,
        workMode = MkplWorkMode.TEST,
        publicationRequest = MkplPublication(
            id = MkplPublicationId("123-234-abc-ABC"),
            title = "abc",
            description = "abc",
            publicationCategory = MkplPublicationCategory.POST,
            visibility = MkplVisibility.VISIBLE_PUBLIC,
            lock = MkplPublicationLock(""),
        ),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(MkplState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("lock", error?.field)
    assertContains(error?.message ?: "", "id")
}

fun validationLockFormat(command: MkplCommand, processor: MkplPublicationProcessor) = runTest {
    val ctx = MkplContext(
        command = command,
        state = MkplState.NONE,
        workMode = MkplWorkMode.TEST,
        publicationRequest = MkplPublication(
            id = MkplPublicationId("123-234-abc-ABC"),
            title = "abc",
            description = "abc",
            publicationCategory = MkplPublicationCategory.POST,
            visibility = MkplVisibility.VISIBLE_PUBLIC,
            lock = MkplPublicationLock("!@#\$%^&*(),.{}"),
        ),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(MkplState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("lock", error?.field)
    assertContains(error?.message ?: "", "id")
}
