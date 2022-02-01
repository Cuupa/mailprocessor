package com.cuupa.mailprocessor.delegates.preprocessing.scan

import com.cuupa.mailprocessor.process.ProcessVariables
import com.cuupa.mailprocessor.services.BarcodeReader
import com.cuupa.mailprocessor.services.BarcodeResult
import com.google.zxing.BarcodeFormat
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.engine.delegate.JavaDelegate

class DetectPatchSheetDelegate(private val barcodeReader: BarcodeReader) : JavaDelegate {

    override fun execute(execution: DelegateExecution?) {
        val variables = ProcessVariables(execution)
        val barcodes = barcodeReader
            .readBarcode(variables.content, variables.contentType, variables.pageDPIs)
            .filter { isPatchSheet(it) }

        variables.hasPatchSheet = barcodes.isNotEmpty()
        variables.patchSheets = barcodes

        log.debug("Found ${barcodes.size} patch sheets")
        log.debug("${DetectPatchSheetDelegate::class.java} triggered")
    }

    private fun isPatchSheet(barcode: BarcodeResult): Boolean {
        return barcode.barcode.barcodeFormat == BarcodeFormat.CODE_39
                && barcode.barcode.text.startsWith("PATCH")
                && isCorrectSuffix(barcode.barcode.text)
    }

    private fun isCorrectSuffix(text: String): Boolean {
        return text.endsWith("1")
                || text.endsWith("2")
                || text.endsWith("3")
                || text.endsWith("4")
                || text.endsWith("T")
    }

    companion object {
        val log: Log = LogFactory.getLog(DetectPatchSheetDelegate::class.java)
    }
}