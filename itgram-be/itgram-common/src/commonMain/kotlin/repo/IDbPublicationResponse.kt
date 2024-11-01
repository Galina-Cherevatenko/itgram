package ru.itgram.common.repo

import ru.itgram.common.models.MkplError
import ru.itgram.common.models.MkplPublication

sealed interface IDbPublicationResponse: IDbResponse<MkplPublication>

data class DbPublicationResponseOk(
    val data: MkplPublication
): IDbPublicationResponse

data class DbPublicationResponseErr(
    val errors: List<MkplError> = emptyList()
): IDbPublicationResponse {
    constructor(err: MkplError): this(listOf(err))
}

data class DbPublicationResponseErrWithData(
    val data: MkplPublication,
    val errors: List<MkplError> = emptyList()
): IDbPublicationResponse {
    constructor(publication: MkplPublication, err: MkplError): this(publication, listOf(err))
}