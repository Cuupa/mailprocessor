package com.cuupa.mailprocessor.services.extractors

import org.apache.tika.Tika
import java.io.ByteArrayInputStream

object Extractors {

    fun get(content: ByteArray): List<String> {
        if (isPdf(content)) {
            return PdfExtractor().extract(content)
        } else if (isEmail(content)) {
            return EmailExtractor().extract(content)
        }
        return listOf()
    }

    private fun isEmail(content: ByteArray): Boolean = tika.detect(ByteArrayInputStream(content)) == rfc822

    private fun isPdf(fileContent: ByteArray): Boolean = fileContent.size > 4 && isPdfHeaderValid(fileContent)

    // header %PDF-
    private fun isPdfHeaderValid(fileContent: ByteArray): Boolean = fileContent[0].toInt() == 0x25 && fileContent[1].toInt() == 0x50 && fileContent[2].toInt() == 0x44 && fileContent[3].toInt() == 0x46 && fileContent[4].toInt() == 0x2D

    private val tika = Tika()

    private const val rfc822 = "message/rfc822"
}