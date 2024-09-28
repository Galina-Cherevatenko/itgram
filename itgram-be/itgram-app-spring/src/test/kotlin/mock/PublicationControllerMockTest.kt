package ru.itgram.app.spring.mock

import ru.itgram.app.spring.config.PublicationConfig
import ru.itgram.app.spring.controllers.PublicationController
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.mockito.kotlin.any
import org.mockito.kotlin.wheneverBlocking
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters
import ru.itgram.biz.MkplPublicationProcessor
import ru.itgram.common.MkplContext
import ru.itgram.common.models.MkplPublicationCategory
import kotlin.test.Test
import ru.itgram.api.v1.models.*
import src.main.kotlin.*

@WebFluxTest(PublicationController::class, PublicationConfig::class)
internal class PublicationControllerMockTest {
    @Autowired
    private lateinit var webClient: WebTestClient

    @Suppress("unused")
    @MockBean
    private lateinit var processor: MkplPublicationProcessor

    @BeforeEach
    fun tearUp() {
        wheneverBlocking { processor.exec(any()) }.then {
            it.getArgument<MkplContext>(0).apply {
                publicationResponse = MkplPublicationStub.get()
                publicationsResponse = MkplPublicationStub.prepareSearchList("sdf", MkplPublicationCategory.POST).toMutableList()
            }
        }
    }

    @Test
    fun createPublication() = testStubPublication(
        "/v1/publication/create",
        PublicationCreateRequest(),
        MkplContext(publicationResponse = MkplPublicationStub.get()).toTransportCreate().copy(responseType = "create")
    )

    @Test
    fun readPublication() = testStubPublication(
        "/v1/publication/read",
        PublicationReadRequest(),
        MkplContext(publicationResponse = MkplPublicationStub.get()).toTransportRead().copy(responseType = "read")
    )

    @Test
    fun updatePublication() = testStubPublication(
        "/v1/publication/update",
        PublicationUpdateRequest(),
        MkplContext(publicationResponse = MkplPublicationStub.get()).toTransportUpdate().copy(responseType = "update")
    )

    @Test
    fun deletePublication() = testStubPublication(
        "/v1/publication/delete",
        PublicationDeleteRequest(),
        MkplContext(publicationResponse = MkplPublicationStub.get()).toTransportDelete().copy(responseType = "delete")
    )

    @Test
    fun searchPublication() = testStubPublication(
        "/v1/publication/search",
        PublicationSearchRequest(),
        MkplContext(publicationsResponse = MkplPublicationStub.prepareSearchList("sdf", MkplPublicationCategory.POST).toMutableList())
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
