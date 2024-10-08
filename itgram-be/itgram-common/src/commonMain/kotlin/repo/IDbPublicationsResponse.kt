package ru.itgram.common.repo

import ru.itgram.common.models.MkplError
import ru.itgram.common.models.MkplPublication

sealed interface IDbPublicationsResponse: IDbResponse<List<MkplPublication>>

data class DbPublicationsResponseOk(
    val data: List<MkplPublication>
): IDbPublicationsResponse

@Suppress("unused")
data class DbPublicationsResponseErr(
    val errors: List<MkplError> = emptyList()
): IDbPublicationsResponse {
    constructor(err: MkplError): this(listOf(err))
}