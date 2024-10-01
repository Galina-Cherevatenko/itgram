package ru.itgram.common.helpers

import ru.itgram.common.MkplContext
import ru.itgram.common.models.MkplError
import ru.itgram.common.models.MkplState
import ru.itgram.logging.common.LogLevel

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

inline fun errorValidation(
    field: String,
    violationCode: String,
    description: String,
    level: LogLevel = LogLevel.ERROR,
) = MkplError(
    code = "validation-$field-$violationCode",
    field = field,
    group = "validation",
    message = "Validation error for field $field: $description",
    level = level,
)