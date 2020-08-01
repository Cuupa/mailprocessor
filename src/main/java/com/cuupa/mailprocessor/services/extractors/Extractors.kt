package com.cuupa.mailprocessor.services.extractors

import org.apache.tika.Tika
import java.io.ByteArrayInputStream

object Extractors {

    fun get(content: ByteArray): List<String> {
        return when {
            isPdf(content) -> PdfExtractor().extract(content)
            isEmail(content) -> EmailExtractor().extract(content)
            else -> listOf()
        }
    }

    fun isEmail(contentType: String) = contentType == rfc822

    fun isZip(contentType: String) = contentType == application_zip

    fun isPdf(fileContent: ByteArray) = fileContent.size > 4 && isPdfHeaderValid(fileContent)

    // header %PDF-
    private fun isPdfHeaderValid(
            fileContent: ByteArray): Boolean = fileContent[0].toInt() == 0x25 && fileContent[1].toInt() == 0x50 && fileContent[2].toInt() == 0x44 && fileContent[3].toInt() == 0x46 && fileContent[4].toInt() == 0x2D

    fun isEmail(content: ByteArray): Boolean = isEmail(tika.detect(ByteArrayInputStream(content)))

    fun isZip(content: ByteArray) = isZip(tika.detect(ByteArrayInputStream(content)))

    private val tika = Tika()

    private const val rfc822 = "message/rfc822"

    private const val application_zip = "application/zip"
}