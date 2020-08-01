package com.cuupa.mailprocessor.services.input.scan

import com.cuupa.mailprocessor.services.extractors.Extractors
import com.cuupa.mailprocessor.services.input.*

object Documents {

    fun get(fileContent: ByteArray): Document {
        return when{
            Extractors.isPdf(fileContent) -> PDF(fileContent)
            Extractors.isEmail(fileContent) -> EMail(fileContent)
            Extractors.isZip(fileContent) -> Zip(fileContent)
            else -> Unknown()
        }
    }

}
