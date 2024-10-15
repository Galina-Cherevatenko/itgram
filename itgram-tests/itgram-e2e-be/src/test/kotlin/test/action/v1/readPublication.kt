package test.action.v1

import io.kotest.assertions.asClue
import io.kotest.assertions.withClue
import io.kotest.matchers.should
import io.kotest.matchers.shouldNotBe
import fixture.client.Client
import ru.itgram.api.v1.models.*
import test.action.beValidId

suspend fun Client.readPublication(id: String?, debug: PublicationDebug = debugStubV1): PublicationResponseObject = readPublication(id, debug = debug) {
    it should haveSuccessResult
    it.publication shouldNotBe null
    it.publication!!
}

suspend fun <T> Client.readPublication(id: String?, debug: PublicationDebug = debugStubV1, block: (PublicationReadResponse) -> T): T =
    withClue("readPublicationV1: $id") {
        id should beValidId

        val response = sendAndReceive(
            "publication/read",
            PublicationReadRequest(
                requestType = "read",
                debug = debug,
                publication = PublicationReadObject(id = id)
            )
        ) as PublicationReadResponse

        response.asClue(block)
    }
