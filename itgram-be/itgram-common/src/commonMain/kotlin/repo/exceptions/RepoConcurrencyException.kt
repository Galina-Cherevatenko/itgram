package ru.itgram.common.repo.exceptions

import ru.itgram.common.models.MkplPublicationId
import ru.itgram.common.models.MkplPublicationLock

class RepoConcurrencyException(id: MkplPublicationId, expectedLock: MkplPublicationLock, actualLock: MkplPublicationLock?): RepoPublicationException(
    id,
    "Expected lock is $expectedLock while actual lock in db is $actualLock"
)