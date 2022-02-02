package com.cuupa.mailprocessor.services.input.scan

import com.cuupa.mailprocessor.services.extractors.Extractors
import com.cuupa.mailprocessor.services.extractors.FiletypeDetector
import com.cuupa.mailprocessor.services.input.Document
import com.cuupa.mailprocessor.services.input.EMail
import com.cuupa.mailprocessor.services.input.PDF
import com.cuupa.mailprocessor.services.input.Unknown

object Documents {

    fun get(fileContent: ByteArray): Document {
        return when{
            FiletypeDetector.isPdf(fileContent) -> PDF(fileContent)
            Extractors.isEmail(fileContent) -> EMail(fileContent)
            else -> Unknown()
        }
    }

}
