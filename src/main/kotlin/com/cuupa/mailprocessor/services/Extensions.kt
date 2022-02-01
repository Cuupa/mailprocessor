package com.cuupa.mailprocessor.services

import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.text.PDFTextStripper

object Extensions {

    fun ByteArray?.isNullOrEmpty() = this?.isEmpty() ?: true

    fun PDDocument.getText(): List<String> {
        val pages = mutableListOf<String>()
        for (page in 1..numberOfPages) {
            val stripper = PDFTextStripper().apply {
                startPage = page
                endPage = page
            }
            pages.add(stripper.getText(this))
        }
        return pages
    }

    fun Boolean?.orFalse() = this ?: false
}