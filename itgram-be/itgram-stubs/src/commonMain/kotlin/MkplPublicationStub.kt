import MkplPublicationStubPost.PUBLICATION_POST_1
import MkplPublicationStubPost.PUBLICATION_START_1
import ru.itgram.common.models.MkplPublication
import ru.itgram.common.models.MkplPublicationCategory
import ru.itgram.common.models.MkplPublicationId

object MkplPublicationStubMkplAdStub {
    fun get(): MkplPublication = PUBLICATION_POST_1.copy()

    fun prepareResult(block: MkplPublication.() -> Unit): MkplPublication = get().apply(block)

    fun prepareSearchList(filter: String, category: MkplPublicationCategory) = listOf(
        mkplPublicationPost("d-666-01", filter, category),
        mkplPublicationPost("d-666-02", filter, category),
        mkplPublicationPost("d-666-03", filter, category),
        mkplPublicationPost("d-666-04", filter, category),
        mkplPublicationPost("d-666-05", filter, category),
        mkplPublicationPost("d-666-06", filter, category),
    )

    private fun mkplPublicationPost(id: String, filter: String, category: MkplPublicationCategory) =
        mkplPublication(PUBLICATION_POST_1, id = id, filter = filter, category = category)

    private fun mkplPublicationStart(id: String, filter: String, category: MkplPublicationCategory) =
        mkplPublication(PUBLICATION_START_1, id = id, filter = filter, category = category)

    private fun mkplPublication(base: MkplPublication, id: String, filter: String, category: MkplPublicationCategory) = base.copy(
        id = MkplPublicationId(id),
        title = "$filter $id",
        description = "desc $filter $id",
        publicationCategory = category,
    )

}