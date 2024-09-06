package ru.itgram.common.models

import ru.itgram.logging.common.LogLevel

data class MkplError(
    val code: String = "",
    val group: String = "",
    val field: String = "",
    val message: String = "",
    val level: LogLevel = LogLevel.ERROR,
    val exception: Throwable? = null,
)
