package com.cuupa.mailprocessor.delegates.preprocessing.scan

import com.cuupa.mailprocessor.process.ProcessVariables
import com.cuupa.mailprocessor.services.BarcodeResult
import com.cuupa.mailprocessor.services.files.content.FileFacade
import com.cuupa.mailprocessor.services.files.util.PageImage
import com.google.zxing.BarcodeFormat
import org.apache.pdfbox.cos.COSDictionary
import org.apache.pdfbox.cos.COSName
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPage
import org.apache.pdfbox.pdmodel.PDPageContentStream
import org.apache.pdfbox.pdmodel.common.PDStream
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory
import org.apache.pdfbox.rendering.PDFRenderer
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.engine.delegate.JavaDelegate
import java.awt.color.ColorSpace
import java.awt.image.BufferedImage
import java.awt.image.ColorConvertOp
import java.io.ByteArrayOutputStream


class ColorToggleDelegate : JavaDelegate {

    override fun execute(execution: DelegateExecution?) {
        val variables = ProcessVariables(execution)
        val patchSheets = variables.patchSheets.filter { isColorToggle(it) }
        val colorToggleList = patchSheets.map { it.pageIndex }

       val file = FileFacade.content(variables.content).handleColorTogglePage(colorToggleList, variables.pageDPIs)
        variables.content = file.content
    }

    private fun isColorToggle(it: BarcodeResult) =
        it.barcode.barcodeFormat == BarcodeFormat.CODE_39 && it.barcode.text == colorToggleCode

    companion object {
        const val colorToggleCode = "PATCH4"
    }
}