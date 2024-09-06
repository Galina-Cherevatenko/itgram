package ru.itgram.common

import ru.itgram.logging.common.MpLoggerProvider


data class MkplCorSettings(
    val loggerProvider: MpLoggerProvider = MpLoggerProvider(),
) {
    companion object {
        val NONE = MkplCorSettings()
    }
}
