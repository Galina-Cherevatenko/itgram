package ru.itgram.common.repo

import ru.itgram.common.helpers.errorSystem

abstract class PublicationRepoBase: IRepoPublication {

    protected suspend fun tryPublicationMethod(block: suspend () -> IDbPublicationResponse) = try {
        block()
    } catch (e: Throwable) {
        DbPublicationResponseErr(errorSystem("methodException", e = e))
    }

    protected suspend fun tryPublicationsMethod(block: suspend () -> IDbPublicationsResponse) = try {
        block()
    } catch (e: Throwable) {
        DbPublicationsResponseErr(errorSystem("methodException", e = e))
    }

}