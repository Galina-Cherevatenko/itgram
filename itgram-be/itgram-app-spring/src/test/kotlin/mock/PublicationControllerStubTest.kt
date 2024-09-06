package ru.itgram.app.spring.mock

import ru.itgram.app.spring.config.PublicationConfig
import ru.itgram.app.spring.controllers.PublicationController
import org.assertj.core.api.Assertions.assertThat
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters
import ru.itgram.api.v1.models.*
import ru.itgram.common.MkplContext
import ru.itgram.common.models.MkplPublicationCategory
import ru.itgram.common.models.MkplState
import src.main.kotlin.*
import kotlin.test.Test

@WebFluxTest(PublicationController::class, PublicationConfig::class)
internal class PublicationControllerStubTest {
    @Autowired
    private lateinit var webClient: WebTestClient

    @Test
    fun createPublication() = testStubPublication(
        "/v1/publication/create",
        PublicationCreateRequest(),
        MkplContext(publicationResponse = MkplPublicationStub.get(), state = MkplState.FINISHING)
            .toTransportCreate().copy(responseType = "create")
    )

    @Test
    fun readPublication() = testStubPublication(
        "/v1/publication/read",
        PublicationReadRequest(),
        MkplContext(publicationResponse = MkplPublicationStub.get(), state = MkplState.FINISHING)
            .toTransportRead().copy(responseType = "read")
    )

    @Test
    fun updatePublication() = testStubPublication(
        "/v1/publication/update",
        PublicationUpdateRequest(),
        MkplContext(publicationResponse = MkplPublicationStub.get(), state = MkplState.FINISHING)
            .toTransportUpdate().copy(responseType = "update")
    )

    @Test
    fun deletePublication() = testStubPublication(
        "/v1/publication/delete",
        PublicationDeleteRequest(),
        MkplContext(publicationResponse = MkplPublicationStub.get(), state = MkplState.FINISHING)
            .toTransportDelete().copy(responseType = "delete")
    )

    @Test
    fun searchPublication() = testStubPublication(
        "/v1/publication/search",
        PublicationSearchRequest(),
        MkplContext(
            publicationsResponse = MkplPublicationStub.prepareSearchList("publication search", MkplPublicationCategory.POST).toMutableList(),
            state = MkplState.FINISHING
        )
            .toTransportSearch().copy(responseType = "search")
    )

    private inline fun <reified Req : Any, reified Res : Any> testStubPublication(
        url: String,
        requestObj: Req,
        responseObj: Res,
    ) {
        webClient
            .post()
            .uri(url)
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(requestObj))
            .exchange()
            .expectStatus().isOk
            .expectBody(Res::class.java)
            .value {
                println("RESPONSE: $it")
                assertThat(it).isEqualTo(responseObj)
            }
    }
}
