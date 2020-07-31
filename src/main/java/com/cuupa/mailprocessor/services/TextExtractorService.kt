package com.cuupa.mailprocessor.services

import com.cuupa.mailprocessor.services.extractors.Extractors
import com.cuupa.mailprocessor.services.input.Attachment

class TextExtractorService {

    fun extract(fileContent: ByteArray, attachments: List<Attachment>): List<String> {
        val plainText = Extractors.get(fileContent).toMutableList()
        plainText.addAll(getAttachmentContent(attachments))
        return plainText
    }

    private fun getAttachmentContent(attachments: List<Attachment>): List<String> {
        val value = mutableListOf<String>()
        attachments.forEach(getAttachmentText(value))
        return value
    }

    private fun getAttachmentText(value: MutableList<String>): (Attachment) -> Unit {
        return { attachment ->
            value.addAll(Extractors.get(attachment.content!!))
        }
    }
}
