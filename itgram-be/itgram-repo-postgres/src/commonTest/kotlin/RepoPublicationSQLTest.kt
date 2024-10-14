package ru.itgram.backend.repo.postgresql

import ru.itgram.RepoPublicationCreateTest
import ru.itgram.RepoPublicationReadTest
import ru.itgram.RepoPublicationUpdateTest
import ru.itgram.RepoPublicationDeleteTest
import ru.itgram.RepoPublicationSearchTest
import ru.itgram.common.repo.IRepoPublication
import ru.itgram.repo.common.IRepoPublicationInitializable
import ru.itgram.repo.common.PublicationRepoInitialized
import kotlin.test.AfterTest

private fun IRepoPublication.clear() {
    val pgRepo = (this as PublicationRepoInitialized).repo as RepoPublicationSql
    pgRepo.clear()
}

class RepoPublicationSQLCreateTest : RepoPublicationCreateTest() {
    override val repo: IRepoPublicationInitializable = SqlTestCompanion.repoUnderTestContainer(
        initObjects,
        randomUuid = { uuidNew.asString() },
    )
    @AfterTest
    fun tearDown() = repo.clear()
}

class RepoPublicationSQLReadTest : RepoPublicationReadTest() {
    override val repo: IRepoPublication = SqlTestCompanion.repoUnderTestContainer(initObjects)
    @AfterTest
    fun tearDown() = repo.clear()
}

class RepoPublicationSQLUpdateTest : RepoPublicationUpdateTest() {
    override val repo: IRepoPublication = SqlTestCompanion.repoUnderTestContainer(
        initObjects,
        randomUuid = { lockNew.asString() },
    )
    @AfterTest
    fun tearDown() {
        repo.clear()
    }
}

class RepoPublicationSQLDeleteTest : RepoPublicationDeleteTest() {
    override val repo: IRepoPublication = SqlTestCompanion.repoUnderTestContainer(initObjects)
    @AfterTest
    fun tearDown() = repo.clear()
}

class RepoAdSQLSearchTest : RepoPublicationSearchTest() {
    override val repo: IRepoPublication = SqlTestCompanion.repoUnderTestContainer(initObjects)
    @AfterTest
    fun tearDown() = repo.clear()
}
