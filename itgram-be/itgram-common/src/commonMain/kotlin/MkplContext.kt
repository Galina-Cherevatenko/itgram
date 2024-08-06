package ru.itgram.common

import kotlinx.datetime.Instant
import ru.itgram.common.models.*
import ru.itgram.common.stubs.MkplStubs

data class MkplContext(
    var command: MkplCommand = MkplCommand.NONE,
    var state: MkplState = MkplState.NONE,
    val errors: MutableList<MkplError> = mutableListOf(),

    var workMode: MkplWorkMode = MkplWorkMode.PROD,
    var stubCase: MkplStubs = MkplStubs.NONE,

    var requestId: MkplRequestId = MkplRequestId.NONE,
    var timeStart: Instant = Instant.NONE,
    var publicationRequest: MkplPublication = MkplPublication(),
    var publicationFilterRequest: MkplPublicationFilter = MkplPublicationFilter(),

    var publicationResponse: MkplPublication = MkplPublication(),
    var publicationsResponse: MutableList<MkplPublication> = mutableListOf(),

    )