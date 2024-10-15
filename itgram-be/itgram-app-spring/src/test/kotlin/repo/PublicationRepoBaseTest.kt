package ru.itgram.app.spring.repo

import org.assertj.core.api.Assertions.assertThat
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters
import ru.itgram.api.v1.models.*
import ru.itgram.common.MkplContext
import ru.itgram.common.models.*
import src.main.kotlin.*
import kotlin.test.Test

internal abstract class PublicationRepoBaseTest {
    protected abstract var webClient: WebTestClient
    private val debug = PublicationDebug(mode = PublicationRequestDebugMode.TEST)
    protected val uuidNew = "10000000-0000-0000-0000-000000000001"

    @Test
    open fun createPublication() = testRepoPublication(
        "create",
        PublicationCreateRequest(
            publication = MkplPublicationStub.get().toTransportCreate(),
            debug = debug,
        ),
        prepareCtx(MkplPublicationStub.prepareResult {
            id = MkplPublicationId(uuidNew)
            ownerId = MkplUserId.NONE
            lock = MkplPublicationLock.NONE
        })
            .toTransportCreate()
            .copy(responseType = "create")
    )

    @Test
    open fun readPublication() = testRepoPublication(
        "read",
        PublicationReadRequest(
            publication = MkplPublicationStub.get().toTransportRead(),
            debug = debug,
        ),
        prepareCtx(MkplPublicationStub.get())
            .toTransportRead()
            .copy(responseType = "read")
    )

    @Test
    open fun updatePublication() = testRepoPublication(
        "update",
        PublicationUpdateRequest(
            publication = MkplPublicationStub.prepareResult { title = "add" }.toTransportUpdate(),
            debug = debug,
        ),
        prepareCtx(MkplPublicationStub.prepareResult { title = "add" })
            .toTransportUpdate().copy(responseType = "update")
    )

    @Test
    open fun deletePublication() = testRepoPublication(
        "delete",
        PublicationDeleteRequest(
            publication = MkplPublicationStub.get().toTransportDelete(),
            debug = debug,
        ),
        prepareCtx(MkplPublicationStub.get())
            .toTransportDelete()
            .copy(responseType = "delete")
    )

    @Test
    open fun searchPublication() = testRepoPublication(
        "search",
        PublicationSearchRequest(
            publicationFilter = PublicationSearchFilter(publicationCategory = PublicationCategory.START),
            debug = debug,
        ),
        MkplContext(
            state = MkplState.RUNNING,
            publicationsResponse = MkplPublicationStub.prepareSearchList("xx", MkplPublicationCategory.START)
                .onEach { it.permissionsClient.clear() }
                .sortedBy { it.id.asString() }
                .toMutableList()
        )
            .toTransportSearch().copy(responseType = "search")
    )

    private fun prepareCtx(publication: MkplPublication) = MkplContext(
        state = MkplState.RUNNING,
        publicationResponse = publication.apply {
            // Пока не реализована эта функциональность
            permissionsClient.clear()
        },
    )

    private inline fun <reified Req : Any, reified Res : IResponse> testRepoPublication(
        url: String,
        requestObj: Req,
        expectObj: Res,
    ) {
        webClient
            .post()
            .uri("/v1/publication/$url")
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(requestObj))
            .exchange()
            .expectStatus().isOk
            .expectBody(Res::class.java)
            .value {
                println("RESPONSE: $it")
                val sortedResp: IResponse = when (it) {
                    is PublicationSearchResponse -> it.copy(publications = it.publications?.sortedBy { it.id })
                    else -> it
                }
                assertThat(sortedResp).isEqualTo(expectObj)
            }
    }
}
