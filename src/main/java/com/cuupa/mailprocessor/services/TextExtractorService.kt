package com.cuupa.mailprocessor.services

import com.cuupa.mailprocessor.services.input.Attachment
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.text.PDFTextStripper
import org.apache.tika.Tika
import java.io.ByteArrayInputStream
import java.util.*

class TextExtractorService {

    fun extract(fileContent: ByteArray, attachments: List<Attachment>): List<String> {
        val plainText = mutableListOf<String>()
        plainText.addAll(getMainContent(fileContent))
        plainText.addAll(getAttachmentContent(attachments))
        return plainText
    }

    private fun getAttachmentContent(attachments: List<Attachment>): List<String> {
        val value = mutableListOf<String>()
        attachments.forEach { attachment ->
            if (isPdf(attachment.content!!)) {
                try {
                    PDDocument.load(ByteArrayInputStream(attachment.content)).use { value.addAll(getTextPerPage(it)) }
                } catch (exception: Exception) {

                }
            } else {
                value.add(Tika().parseToString(ByteArrayInputStream(attachment.content)))
            }
        }
        return value
    }

    private fun getMainContent(fileContent: ByteArray): List<String> {
        val value = mutableListOf<String>()
        if (isPdf(fileContent)) {
            try {
                PDDocument.load(ByteArrayInputStream(fileContent)).use { value.addAll(getTextPerPage(it)) }
            } catch (exception: Exception) {

            }
        } else {
            value.add(Tika().parseToString(ByteArrayInputStream(fileContent)))
        }
        return value
    }

    private fun isPdf(fileContent: ByteArray): Boolean = fileContent.size > 4 && isPdfHeaderValid(fileContent)

    // header %PDF-
    private fun isPdfHeaderValid(fileContent: ByteArray): Boolean = fileContent[0].toInt() == 0x25 && fileContent[1].toInt() == 0x50 && fileContent[2].toInt() == 0x44 && fileContent[3].toInt() == 0x46 && fileContent[4].toInt() == 0x2D

    private fun getTextPerPage(document: PDDocument): List<String> {
        val pages: MutableList<String> = ArrayList(document.numberOfPages)
        for (page in 0..document.numberOfPages) {
            val stripper = PDFTextStripper()
            stripper.startPage = page
            stripper.endPage = page
            pages.add(stripper.getText(document))
        }
        return pages
    }

}
