import ru.itgram.api.v1.models.*
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class ResponseV1SerializationTest {
    private val response = PublicationCreateResponse(
        publication = PublicationResponseObject(
            title = "title",
            description = "description",
            publicationCategory = PublicationCategory.POST,
            visibility = PublicationVisibility.PUBLIC,
        )
    )

    @Test
    fun serialize() {
        val json = apiV1Mapper.writeValueAsString(response)

        assertContains(json, Regex("\"title\":\\s*\"title\""))
        assertContains(json, Regex("\"responseType\":\\s*\"create\""))
    }

    @Test
    fun deserialize() {
        val json = apiV1Mapper.writeValueAsString(response)
        val obj = apiV1Mapper.readValue(json, IResponse::class.java) as PublicationCreateResponse

        assertEquals(response, obj)
    }
}