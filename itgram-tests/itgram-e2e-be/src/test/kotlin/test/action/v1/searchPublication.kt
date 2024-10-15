package test.action.v1

import io.kotest.assertions.asClue
import io.kotest.assertions.withClue
import io.kotest.matchers.should
import fixture.client.Client
import ru.itgram.api.v1.models.*

suspend fun Client.searchPublication(
    search: PublicationSearchFilter,
    debug: PublicationDebug = debugStubV1
): List<PublicationResponseObject> = searchPublication(search, debug = debug) {
    it should haveSuccessResult
    it.publications ?: listOf()
}

suspend fun <T> Client.searchPublication(
    search: PublicationSearchFilter,
    debug: PublicationDebug = debugStubV1,
    block: (PublicationSearchResponse) -> T
): T =
    withClue("searchPublicationV1: $search") {
        val response = sendAndReceive(
            "publication/search",
            PublicationSearchRequest(
                requestType = "search",
                debug = debug,
                publicationFilter = search,
            )
        ) as PublicationSearchResponse

        response.asClue(block)
    }