import org.junit.Test
import ru.itgram.api.v1.models.*
import ru.itgram.common.MkplContext
import src.main.kotlin.fromTransport
import src.main.kotlin.toTransportPublication
import ru.itgram.common.models.*
import ru.itgram.common.stubs.MkplStubs
import kotlin.test.assertEquals

class MapperTest {

    @Test
    fun fromTransport() {
        val req = PublicationCreateRequest(
            debug = PublicationDebug(
                mode = PublicationRequestDebugMode.STUB,
                stub = PublicationRequestDebugStubs.SUCCESS,
            ),
            publication = PublicationCreateObject(
                title = "title",
                description = "desc",
                publicationCategory = PublicationCategory.POST,
                visibility = PublicationVisibility.PUBLIC,
            ),
        )

        val context = MkplContext()
        context.fromTransport(req)

        assertEquals(MkplStubs.SUCCESS, context.stubCase)
        assertEquals(MkplWorkMode.STUB, context.workMode)
        assertEquals("title", context.publicationRequest.title)
        assertEquals(MkplVisibility.VISIBLE_PUBLIC, context.publicationRequest.visibility)
        assertEquals(MkplPublicationCategory.POST, context.publicationRequest.publicationCategory)
    }

    @Test
    fun toTransport() {
        val context = MkplContext(
            requestId = MkplRequestId("1234"),
            command = MkplCommand.CREATE,
            publicationResponse = MkplPublication(
                title = "title",
                description = "desc",
                publicationCategory = MkplPublicationCategory.POST,
                visibility = MkplVisibility.VISIBLE_PUBLIC,
            ),
            errors = mutableListOf(
                MkplError(
                    code = "err",
                    group = "request",
                    field = "title",
                    message = "wrong title",
                )
            ),
            state = MkplState.RUNNING,
        )

        val req = context.toTransportPublication() as PublicationCreateResponse

        assertEquals("title", req.publication?.title)
        assertEquals("desc", req.publication?.description)
        assertEquals(PublicationVisibility.PUBLIC, req.publication?.visibility)
        assertEquals(PublicationCategory.POST, req.publication?.publicationCategory)
        assertEquals(1, req.errors?.size)
        assertEquals("err", req.errors?.firstOrNull()?.code)
        assertEquals("request", req.errors?.firstOrNull()?.group)
        assertEquals("title", req.errors?.firstOrNull()?.field)
        assertEquals("wrong title", req.errors?.firstOrNull()?.message)
    }
}
