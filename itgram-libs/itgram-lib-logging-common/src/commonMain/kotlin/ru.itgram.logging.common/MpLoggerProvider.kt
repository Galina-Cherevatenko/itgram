package ru.itgram.logging.common

import kotlin.reflect.KClass
import kotlin.reflect.KFunction

class MpLoggerProvider(
    private val provider: (String) -> IMpLogWrapper = { IMpLogWrapper.DEFAULT }
) {
    /**
     * Инициализирует и возвращает экземпляр логера
     */
    fun logger(loggerId: String): IMpLogWrapper = provider(loggerId)

    /**
     * Инициализирует и возвращает экземпляр логера
     */
    fun logger(clazz: KClass<*>): IMpLogWrapper = provider(clazz.qualifiedName ?: clazz.simpleName ?: "(unknown)")

    /**
     * Инициализирует и возвращает экземпляр логера
     */
    fun logger(function: KFunction<*>): IMpLogWrapper = provider(function.name)
}

