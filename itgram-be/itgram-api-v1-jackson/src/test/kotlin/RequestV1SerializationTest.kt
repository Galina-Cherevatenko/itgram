import junit.framework.TestCase.assertEquals
import ru.itgram.api.v1.models.*
import kotlin.test.Test
import kotlin.test.assertContains

class RequestV1SerializationTest {
    private val request = PublicationCreateRequest(
        debug = PublicationDebug(
            mode = PublicationRequestDebugMode.STUB,
            stub = PublicationRequestDebugStubs.BAD_TITLE
        ),
        publication = PublicationCreateObject(
            title = "title",
            description = "description",
            publicationCategory = PublicationCategory.POST,
            visibility = PublicationVisibility.PUBLIC,
        )
    )

    @Test
    fun serialize() {
        val json = apiV1Mapper.writeValueAsString(request)

        assertContains(json, Regex("\"title\":\\s*\"title\""))
        assertContains(json, Regex("\"mode\":\\s*\"stub\""))
        assertContains(json, Regex("\"stub\":\\s*\"badTitle\""))
        assertContains(json, Regex("\"requestType\":\\s*\"create\""))
    }

    @Test
    fun deserialize() {
        val json = apiV1Mapper.writeValueAsString(request)
        val obj = apiV1Mapper.readValue(json, IRequest::class.java) as PublicationCreateRequest

        assertEquals(request, obj)
    }

    @Test
    fun deserializeNaked() {
        val jsonString = """
            {"publication": null}
        """.trimIndent()
        val obj = apiV1Mapper.readValue(jsonString, PublicationCreateRequest::class.java)

        assertEquals(null, obj.publication)
    }
}