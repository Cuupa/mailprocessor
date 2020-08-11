package com.cuupa.mailprocessor.services.input

import java.io.Serializable

open class Document(var content:ByteArray?) : Serializable {
    lateinit var user: String
    lateinit var filename: String

    override fun equals(other: Any?): Boolean {
        return when (other) {
            null -> false
            !is Document -> false
            other.user == user && other.filename == filename && contentEquals(other) -> true
            else -> false
        }
    }

    private fun contentEquals(other: Document): Boolean {
        return when {
            other.content == null && content == null -> true
            other.content != null && content == null -> false
            other.content != null -> other.content!!.contentEquals(content!!)
            else -> false
        }
    }

    override fun hashCode(): Int {
        var result = user.hashCode()
        result = 31 * result + filename.hashCode()
        result = 31 * result + (content?.contentHashCode() ?: 0)
        return result
    }
}
