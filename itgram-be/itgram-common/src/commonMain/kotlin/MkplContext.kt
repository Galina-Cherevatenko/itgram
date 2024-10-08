package ru.itgram.common

import kotlinx.datetime.Instant
import ru.itgram.common.models.*
import ru.itgram.common.repo.IRepoPublication
import ru.itgram.common.stubs.MkplStubs

data class MkplContext(
    var command: MkplCommand = MkplCommand.NONE,
    var state: MkplState = MkplState.NONE,
    val errors: MutableList<MkplError> = mutableListOf(),

    var corSettings: MkplCorSettings = MkplCorSettings(),
    var workMode: MkplWorkMode = MkplWorkMode.PROD,
    var stubCase: MkplStubs = MkplStubs.NONE,

    var requestId: MkplRequestId = MkplRequestId.NONE,
    var timeStart: Instant = Instant.NONE,
    var publicationRequest: MkplPublication = MkplPublication(),
    var publicationFilterRequest: MkplPublicationFilter = MkplPublicationFilter(),

    var publicationValidating: MkplPublication = MkplPublication(),
    var publicationFilterValidating: MkplPublicationFilter = MkplPublicationFilter(),

    var publicationValidated: MkplPublication = MkplPublication(),
    var publicationFilterValidated: MkplPublicationFilter = MkplPublicationFilter(),

    var publicationRepo: IRepoPublication = IRepoPublication.NONE,
    var publicationRepoRead: MkplPublication = MkplPublication(), // То, что прочитали из репозитория
    var publicationRepoPrepare: MkplPublication = MkplPublication(), // То, что готовим для сохранения в БД
    var publicationRepoDone: MkplPublication = MkplPublication(),  // Результат, полученный из БД
    var publicationsRepoDone: MutableList<MkplPublication> = mutableListOf(),

    var publicationResponse: MkplPublication = MkplPublication(),
    var publicationsResponse: MutableList<MkplPublication> = mutableListOf(),
)
