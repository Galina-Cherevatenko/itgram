package test.action.v1

import fixture.client.Client
import io.kotest.assertions.asClue
import io.kotest.assertions.withClue
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import ru.itgram.api.v1.models.*
import test.action.beValidId
import test.action.beValidLock

suspend fun Client.deletePublication(publication: PublicationResponseObject, debug: PublicationDebug = debugStubV1) {
    val id = publication.id
    val lock = publication.lock
    withClue("deletePublicationV2: $id, lock: $lock") {
        id should beValidId
        lock should beValidLock

        val response = sendAndReceive(
            "publication/delete",
            PublicationDeleteRequest(
                debug = debug,
                publication = PublicationDeleteObject(id = id, lock = lock)
            )
        ) as PublicationDeleteResponse

        response.asClue {
            response should haveSuccessResult
            response.publication shouldBe publication
        }
    }
}
