package test.action.v1

import fixture.client.Client
import io.kotest.assertions.asClue
import io.kotest.assertions.withClue
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldMatch
import ru.itgram.api.v1.models.*


suspend fun Client.createPublication(
    publication: PublicationCreateObject = someCreatePublication,
    debug: PublicationDebug = debugStubV1
): PublicationResponseObject = createPublication(publication, debug = debug) {
    it should haveSuccessResult
    it.publication shouldNotBe null
    it.publication?.apply {
        title shouldBe publication.title
        description shouldBe publication.description
        publicationCategory shouldBe publication.publicationCategory
        visibility shouldBe publication.visibility
        id.toString() shouldMatch Regex("^[\\d\\w_-]+\$")
        lock.toString() shouldMatch Regex("^[\\d\\w_-]+\$")
    }
    it.publication!!
}

suspend fun <T> Client.createPublication(
    publication: PublicationCreateObject = someCreatePublication,
    debug: PublicationDebug = debugStubV1,
    block: (PublicationCreateResponse) -> T
): T =
    withClue("createPublicationV1: $publication") {
        val response = sendAndReceive(
            "publication/create", PublicationCreateRequest(
                requestType = "create",
                debug = debug,
                publication = publication
            )
        ) as PublicationCreateResponse

        response.asClue(block)
    }
