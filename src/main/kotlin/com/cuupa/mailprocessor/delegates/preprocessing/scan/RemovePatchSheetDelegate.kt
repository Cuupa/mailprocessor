package com.cuupa.mailprocessor.delegates.preprocessing.scan

import com.cuupa.mailprocessor.process.ProcessVariables
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPage
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.engine.delegate.JavaDelegate
import java.io.ByteArrayOutputStream

class RemovePatchSheetDelegate : JavaDelegate {

    override fun execute(execution: DelegateExecution?) {
        val variables = ProcessVariables(execution)
        val patchSheets = variables.patchSheets

        PDDocument.load(variables.content).use { document ->
            val pages: List<PDPage> = patchSheets.map { document.pages.get(it.pageIndex) }
            pages.forEach { document.pages.remove(it) }
            val outputStream = ByteArrayOutputStream()
            document.save(outputStream)
            variables.content = outputStream.toByteArray()
        }
    }
}