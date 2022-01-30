package com.cuupa.mailprocessor.services.extractors

import org.apache.tika.Tika
import java.io.ByteArrayInputStream

object Extractors {

    fun get(content: ByteArray): List<String> {
        return when {
            //isPdf(content) -> PdfExtractor.extract(content)
            isEmail(content) -> EmailExtractor(tika).extract(content)
            else -> listOf()
        }
    }

    private fun isEmail(contentType: String) = contentType == rfc822

    private fun isZip(contentType: String) = contentType == application_zip

    fun isEmail(content: ByteArray): Boolean = isEmail(tika.detect(ByteArrayInputStream(content)))



    private val tika = Tika()

    private const val rfc822 = "message/rfc822"

    private const val application_zip = "application/zip"
}