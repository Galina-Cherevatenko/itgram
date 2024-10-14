package ru.itgram.app.spring.repo

import PublicationRepoInMemory
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.test.web.reactive.server.WebTestClient
import ru.itgram.app.spring.config.PublicationConfig
import ru.itgram.app.spring.controllers.PublicationController
import ru.itgram.common.repo.IRepoPublication
import com.ninjasquad.springmockk.MockkBean
import io.mockk.coEvery
import io.mockk.slot
import org.springframework.context.annotation.Import
import ru.itgram.common.models.MkplPublicationCategory
import ru.itgram.common.repo.DbPublicationFilterRequest
import ru.itgram.common.repo.DbPublicationIdRequest
import ru.itgram.common.repo.DbPublicationRequest
import ru.itgram.repo.common.PublicationRepoInitialized
import kotlin.test.Test

@WebFluxTest(
    PublicationController::class, PublicationConfig::class,
    properties = ["spring.main.allow-bean-definition-overriding=true"]
)
@Import(RepoInMemoryConfig::class)
internal class PublicationRepoInMemoryTest : PublicationRepoBaseTest() {
    @Autowired
    override lateinit var webClient: WebTestClient

    @MockkBean
    @Qualifier("testRepo")
    lateinit var testTestRepo: IRepoPublication

    @BeforeEach
    fun tearUp() {
        val slotPublication = slot<DbPublicationRequest>()
        val slotId = slot<DbPublicationIdRequest>()
        val slotFl = slot<DbPublicationFilterRequest>()
        val repo = PublicationRepoInitialized(
            repo = PublicationRepoInMemory(randomUuid = { uuidNew }),
            initObjects = MkplPublicationStub.prepareSearchList("xx", MkplPublicationCategory.START) + MkplPublicationStub.get()
        )
        coEvery { testTestRepo.createPublication(capture(slotPublication)) } coAnswers { repo.createPublication(slotPublication.captured) }
        coEvery { testTestRepo.readPublication(capture(slotId)) } coAnswers { repo.readPublication(slotId.captured) }
        coEvery { testTestRepo.updatePublication(capture(slotPublication)) } coAnswers { repo.updatePublication(slotPublication.captured) }
        coEvery { testTestRepo.deletePublication(capture(slotId)) } coAnswers { repo.deletePublication(slotId.captured) }
        coEvery { testTestRepo.searchPublication(capture(slotFl)) } coAnswers { repo.searchPublication(slotFl.captured) }
    }

    @Test
    override fun createPublication() = super.createPublication()

    @Test
    override fun readPublication() = super.readPublication()

    @Test
    override fun updatePublication() = super.updatePublication()

    @Test
    override fun deletePublication() = super.deletePublication()

    @Test
    override fun searchPublication() = super.searchPublication()
}
