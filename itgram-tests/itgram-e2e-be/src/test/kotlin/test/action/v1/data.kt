package test.action.v1

import ru.itgram.api.v1.models.*
import test.TestDebug

val debugStubV1 = PublicationDebug(mode = PublicationRequestDebugMode.STUB, stub = PublicationRequestDebugStubs.SUCCESS)

val someCreatePublication = PublicationCreateObject(
    title = "Требуется болт",
    description = "Требуется болт 100x5 с шестигранной шляпкой",
    publicationCategory = PublicationCategory.POST,
    visibility = PublicationVisibility.PUBLIC
)

fun TestDebug.toV1() = when (this) {
    TestDebug.STUB -> debugStubV1
    TestDebug.PROD -> PublicationDebug(mode = PublicationRequestDebugMode.PROD)
    TestDebug.TEST -> PublicationDebug(mode = PublicationRequestDebugMode.TEST)
}
