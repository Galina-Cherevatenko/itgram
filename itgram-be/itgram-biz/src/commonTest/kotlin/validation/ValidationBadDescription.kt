package ru.itgram.biz.validation

import ru.itgram.biz.MkplPublicationProcessor
import kotlinx.coroutines.test.runTest
import ru.itgram.common.MkplContext
import ru.itgram.common.models.*
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

private val stub = MkplPublicationStub.get()

fun validationDescriptionCorrect(command: MkplCommand, processor: MkplPublicationProcessor) = runTest {
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
    assertEquals("abc", ctx.publicationValidated.description)
}

fun validationDescriptionTrim(command: MkplCommand, processor: MkplPublicationProcessor) = runTest {
    val ctx = MkplContext(
        command = command,
        state = MkplState.NONE,
        workMode = MkplWorkMode.TEST,
        publicationRequest = MkplPublication(
            id = stub.id,
            title = "abc",
            description = " \n\tabc \n\t",
            publicationCategory = MkplPublicationCategory.POST,
            visibility = MkplVisibility.VISIBLE_PUBLIC,
            lock = MkplPublicationLock("123-234-abc-ABC"),
        ),
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(MkplState.FAILING, ctx.state)
    assertEquals("abc", ctx.publicationValidated.description)
}

fun validationDescriptionEmpty(command: MkplCommand, processor: MkplPublicationProcessor) = runTest {
    val ctx = MkplContext(
        command = command,
        state = MkplState.NONE,
        workMode = MkplWorkMode.TEST,
        publicationRequest = MkplPublication(
            id = stub.id,
            title = "abc",
            description = "",
            publicationCategory = MkplPublicationCategory.POST,
            visibility = MkplVisibility.VISIBLE_PUBLIC,
            lock = MkplPublicationLock("123-234-abc-ABC"),
        ),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(MkplState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("description", error?.field)
    assertContains(error?.message ?: "", "description")
}

fun validationDescriptionSymbols(command: MkplCommand, processor: MkplPublicationProcessor) = runTest {
    val ctx = MkplContext(
        command = command,
        state = MkplState.NONE,
        workMode = MkplWorkMode.TEST,
        publicationRequest = MkplPublication(
            id = stub.id,
            title = "abc",
            description = "!@#$%^&*(),.{}",
            publicationCategory = MkplPublicationCategory.POST,
            visibility = MkplVisibility.VISIBLE_PUBLIC,
            lock = MkplPublicationLock("123-234-abc-ABC"),
        ),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(MkplState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("description", error?.field)
    assertContains(error?.message ?: "", "description")
}
