import ru.itgram.*

class PublicationRepoInMemoryTest: RepoPublicationCreateTest() {
    override val repo = PublicationRepoInitialized(
        PublicationRepoInMemory(randomUuid = { uuidNew.asString() }),
        initObjects = initObjects,
    )
}

class PublicationRepoInMemoryDeleteTest : RepoPublicationDeleteTest() {
    override val repo = PublicationRepoInitialized(
        PublicationRepoInMemory(),
        initObjects = initObjects,
    )
}

class PublicationRepoInMemoryReadTest : RepoPublicationReadTest() {
    override val repo = PublicationRepoInitialized(
        PublicationRepoInMemory(),
        initObjects = initObjects,
    )
}

class PublicationRepoInMemorySearchTest : RepoPublicationSearchTest() {
    override val repo = PublicationRepoInitialized(
        PublicationRepoInMemory(),
        initObjects = initObjects,
    )
}

class PublicationRepoInMemoryUpdateTest : RepoPublicationUpdateTest() {
    override val repo = PublicationRepoInitialized(
        PublicationRepoInMemory(),
        initObjects = initObjects,
    )
}