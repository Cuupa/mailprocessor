package com.cuupa.mailprocessor.services

import com.cuupa.mailprocessor.services.extractors.Extractors
import com.cuupa.mailprocessor.services.input.Attachment

class TextExtractorService {

    fun extract(fileContent: ByteArray, attachments: List<Attachment>): List<String> {
        return listOf(Extractors.get(fileContent), attachments.map { Extractors.get(it.content!!) }.flatten()).flatten()
    }
}
