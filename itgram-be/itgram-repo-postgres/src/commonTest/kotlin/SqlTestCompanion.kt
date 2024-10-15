package ru.itgram.backend.repo.postgresql

import com.benasher44.uuid.uuid4
import ru.itgram.common.models.MkplPublication
import ru.itgram.repo.common.PublicationRepoInitialized
import ru.itgram.repo.common.IRepoPublicationInitializable

object SqlTestCompanion {
    private const val HOST = "localhost"
    private const val USER = "postgres"
    private const val PASS = "itgram-pass"
    val PORT = getEnv("postgresPort")?.toIntOrNull() ?: 5432

    fun repoUnderTestContainer(
        initObjects: Collection<MkplPublication> = emptyList(),
        randomUuid: () -> String = { uuid4().toString() },
    ): IRepoPublicationInitializable = PublicationRepoInitialized(
        repo = RepoPublicationSql(
            SqlProperties(
                host = HOST,
                user = USER,
                password = PASS,
                port = PORT,
            ),
            randomUuid = randomUuid
        ),
        initObjects = initObjects,
    )
}

