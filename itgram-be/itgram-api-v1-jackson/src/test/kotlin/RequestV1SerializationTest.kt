package ru.otus.otuskotlin.marketplace.api.v1

import ru.otus.api.v1.models.*
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class RequestV1SerializationTest {
    private val request = PublicationCreateRequest(
        debug = PublicationDebug(
            mode = PublicationRequestDebugMode.STUB,
            stub = PublicationRequestDebugStubs.BAD_TITLE
        ),
        publication = PublicationCreateObject(
            title = "ad title",
            description = "ad description",
            publicationCategory = PublicationCategory.POST,
            visibility = PublicationVisibility.PUBLIC,
        )
    )

    @Test
    fun serialize() {
        val json = apiV1Mapper.writeValueAsString(request)

        assertContains(json, Regex("\"title\":\\s*\"ad title\""))
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
            {"ad": null}
        """.trimIndent()
        val obj = apiV1Mapper.readValue(jsonString, PublicationCreateRequest::class.java)

        assertEquals(null, obj.publication)
    }
}
