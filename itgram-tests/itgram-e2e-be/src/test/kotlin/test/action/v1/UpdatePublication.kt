package test.action.v1

import io.kotest.assertions.asClue
import io.kotest.assertions.withClue
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import fixture.client.Client
import ru.itgram.api.v1.models.*
import test.action.beValidId
import test.action.beValidLock

suspend fun Client.updatePublication(
    publication: PublicationUpdateObject,
    debug: PublicationDebug = debugStubV1
): PublicationResponseObject =
    updatePublication(publication, debug = debug) {
        it should haveSuccessResult
        it.publication shouldNotBe null
        it.publication?.apply {
            if (publication.title != null)
                title shouldBe publication.title
            if (publication.description != null)
                description shouldBe publication.description
            if (publication.publicationCategory != null)
                publicationCategory shouldBe publication.publicationCategory
            if (publication.visibility != null)
                visibility shouldBe publication.visibility
        }
        it.publication!!
    }

suspend fun <T> Client.updatePublication(
    publication: PublicationUpdateObject,
    debug: PublicationDebug = debugStubV1,
    block: (PublicationUpdateResponse) -> T
): T {
    val id = publication.id
    val lock = publication.lock
    return withClue("updatedV1: $id, lock: $lock, set: $publication") {
        id should beValidId
        lock should beValidLock

        val response = sendAndReceive(
            "publication/update", PublicationUpdateRequest(
                debug = debug,
                publication = publication.copy(id = id, lock = lock)
            )
        ) as PublicationUpdateResponse

        response.asClue(block)
    }
}
