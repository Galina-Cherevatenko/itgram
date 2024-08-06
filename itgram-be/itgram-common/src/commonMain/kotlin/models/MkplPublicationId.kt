package ru.itgram.common.models

import kotlin.jvm.JvmInline

@JvmInline
value class MkplPublicationId(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = MkplPublicationId("")
    }
}
