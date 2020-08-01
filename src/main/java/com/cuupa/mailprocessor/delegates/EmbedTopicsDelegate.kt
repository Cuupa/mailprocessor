package com.cuupa.mailprocessor.delegates

import com.cuupa.mailprocessor.process.ProcessInstanceHandler
import com.cuupa.mailprocessor.services.extractors.Extractors
import org.apache.juli.logging.LogFactory
import org.apache.pdfbox.pdmodel.PDDocument
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.engine.delegate.JavaDelegate
import java.io.ByteArrayOutputStream

class EmbedTopicsDelegate : JavaDelegate {

    override fun execute(execution: DelegateExecution?) {
        val handler = ProcessInstanceHandler(execution)
        if (Extractors.isPdf(handler.fileContent)) {
            embeddInPdf(handler)
            log.info("Successfully wrote keywords ${handler.topics.joinToString(", ",
                                                                                "",
                                                                                "")} to file ${handler.fileName}")
        }
    }

    private fun embeddInPdf(handler: ProcessInstanceHandler) {
        PDDocument.load(handler.fileContent).use {
            it.documentInformation.keywords = handler.topics.joinToString(", ", "", "")
            it.isAllSecurityToBeRemoved = true
            handler.fileContent = saveChanges(it)
        }
    }

    private fun saveChanges(it: PDDocument): ByteArray {
        val outputStream = ByteArrayOutputStream()
        it.save(outputStream)
        return outputStream.toByteArray()
    }

    companion object {
        private val log = LogFactory.getLog(EmbedTopicsDelegate::class.java)
    }
}