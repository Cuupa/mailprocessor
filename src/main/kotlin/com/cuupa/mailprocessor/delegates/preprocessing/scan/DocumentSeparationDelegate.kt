package com.cuupa.mailprocessor.delegates.preprocessing.scan

import com.cuupa.mailprocessor.process.ProcessVariables
import com.cuupa.mailprocessor.services.BarcodeResult
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

        variables.patchSheets.filter { isPageSeparationSheet(it) }
    }

    private fun isPageSeparationSheet(it: BarcodeResult): Boolean {
        return it.barcode.text == pageSeparationCodeT || it.barcode.text == pageSeparationCode2
    }

    companion object {
        const val pageSeparationCodeT = "PATCHT"
        const val pageSeparationCode2 = "PATCH2"
        val log: Log = LogFactory.getLog(DocumentSeparationDelegate::class.java)
    }
}