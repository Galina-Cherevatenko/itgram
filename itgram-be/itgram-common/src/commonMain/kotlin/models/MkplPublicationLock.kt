package ru.itgram.common.models

import kotlin.jvm.JvmInline

@JvmInline
value class MkplPublicationLock(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = MkplPublicationLock("")
    }
}
