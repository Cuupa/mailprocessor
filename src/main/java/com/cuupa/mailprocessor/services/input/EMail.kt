package com.cuupa.mailprocessor.services.input

import java.time.LocalDateTime

class EMail(content: ByteArray?) : Document(content) {

    constructor() : this(null)

    var from: String? = null
    var subject: String? = null
    var label: String? = null
    var receivedDate: LocalDateTime? = null
    var attachments = mutableListOf<Attachment>()

    val isValid
        get() = !from.isNullOrEmpty() && receivedDate != null && (content?.isNotEmpty() ?: false)

    override fun equals(other: Any?): Boolean {
        return when (other) {
            null -> false
            !is EMail -> false
            equals(other) && super.equals(other) -> true
            else -> false
        }
    }

    private fun equals(
            other: EMail) = fromEquals(other) && subjectEquals(other) && labelEquals(other) && receivedDateEquals(other) && attachmentsEquals(
        other)

    private fun attachmentsEquals(other: EMail) = attachments == other.attachments

    private fun receivedDateEquals(other: EMail) = other.receivedDate == receivedDate

    private fun labelEquals(other: EMail) = other.label == label

    private fun subjectEquals(other: EMail) = other.subject == subject

    private fun fromEquals(other: EMail) = other.from == from

    override fun hashCode(): Int {
        var result = from?.hashCode() ?: 0
        result = 31 * result + (subject?.hashCode() ?: 0)
        result = 31 * result + (label?.hashCode() ?: 0)
        result = 31 * result + (receivedDate?.hashCode() ?: 0)
        result = 31 * result + attachments.hashCode()
        return result
    }
}
