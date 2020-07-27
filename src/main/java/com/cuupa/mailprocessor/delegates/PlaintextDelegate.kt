package com.cuupa.mailprocessor.delegates

import com.cuupa.mailprocessor.process.ProcessInstanceHandler
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.text.PDFTextStripper
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.engine.delegate.JavaDelegate
import java.io.ByteArrayInputStream
import java.io.IOException
import java.util.*

class PlaintextDelegate : JavaDelegate {

    override fun execute(delegateExecution: DelegateExecution) {
        val handler = ProcessInstanceHandler(delegateExecution)
        if (handler.isScanMail) {
            PDDocument.load(ByteArrayInputStream(handler.fileContent)).use { handler.plainText = getTextPerPage(it) }
        } else {
            handler.attachments.forEach { attachment ->
                try {
                    PDDocument.load(ByteArrayInputStream(attachment.content)).use {
                        val plainText = handler.plainText.toMutableList()
                        plainText.addAll(getTextPerPage(it))
                        handler.plainText = plainText
                    }
                } catch (exception: Exception) {
                    // Propably no PDF
                }
            }
        }
    }

    @Throws(IOException::class)
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