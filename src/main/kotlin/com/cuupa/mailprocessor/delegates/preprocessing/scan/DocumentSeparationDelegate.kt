package com.cuupa.mailprocessor.delegates.preprocessing.scan

import com.cuupa.mailprocessor.process.ProcessVariables
import com.cuupa.mailprocessor.services.BarcodeResult
import com.cuupa.mailprocessor.services.files.content.FileFacade
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.engine.delegate.JavaDelegate

class DocumentSeparationDelegate : JavaDelegate {

    override fun execute(execution: DelegateExecution?) {
        val variables = ProcessVariables(execution)

        if (!variables.hasPatchSheet) {
            log.error("No patch sheets found, although ${DetectPatchSheetDelegate::class.java} has detected some")
        }

        val pageSeparationSheets = variables.patchSheets
            .filter { it.isPageSeparationSheet() }
            .map { it.pageIndex }
        val file = FileFacade.content(variables.content).handleFileSeparationPatchSheet(pageSeparationSheets)
    }

    companion object {
        val log: Log = LogFactory.getLog(DocumentSeparationDelegate::class.java)
    }
}