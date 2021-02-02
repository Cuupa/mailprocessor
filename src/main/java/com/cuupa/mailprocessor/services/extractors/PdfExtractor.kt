package com.cuupa.mailprocessor.services.extractors

import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.text.PDFTextStripper
import java.io.ByteArrayInputStream

class PdfExtractor : TextExtractor {

    override fun extract(content: ByteArray): List<String> {
        return try {
            PDDocument.load(ByteArrayInputStream(content)).use { getTextPerPage(it) }
        } catch (exception: Exception) {
            listOf()
        }
    }

    private fun getTextPerPage(document: PDDocument): List<String> {
        val pages: MutableList<String> = mutableListOf()
        for (page in 0..document.numberOfPages) {
            val stripper = PDFTextStripper()
            stripper.startPage = page
            stripper.endPage = page
            pages.add(stripper.getText(document))
        }
        return pages
    }

}
