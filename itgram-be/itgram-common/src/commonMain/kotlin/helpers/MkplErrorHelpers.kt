package ru.itgram.common.helpers

import ru.itgram.common.MkplContext
import ru.itgram.common.models.MkplError
import ru.itgram.common.models.MkplState

fun Throwable.asMkplError(
    code: String = "unknown",
    group: String = "exceptions",
    message: String = this.message ?: "",
) = MkplError(
    code = code,
    group = group,
    field = "",
    message = message,
    exception = this,
)

inline fun MkplContext.addError(vararg error: MkplError) = errors.addAll(error)

inline fun MkplContext.fail(error: MkplError) {
    addError(error)
    state = MkplState.FAILING
}