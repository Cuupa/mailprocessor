package com.cuupa.mailprocessor.delegates.preprocessing.scan

import com.cuupa.mailprocessor.delegates.AbstractJavaDelegate
import com.cuupa.mailprocessor.process.ProcessVariables
import com.cuupa.mailprocessor.services.BarcodeResult
import com.cuupa.mailprocessor.services.files.content.FileFacade
import com.cuupa.mailprocessor.services.files.util.PageImage
import com.cuupa.mailprocessor.userconfiguration.WorkDirectory
import com.google.zxing.BarcodeFormat
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
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


class ColorToggleDelegate(private val workDirectory: WorkDirectory) : AbstractJavaDelegate() {

    override fun execute(execution: DelegateExecution?) {
        val variables = ProcessVariables(execution)
        val colorToggleList = variables.patchSheets
            .filter { it.isColorToggle() }
            .map { it.pageIndex }

        val content = getContent(variables.id!!, workDirectory)

        val file = FileFacade.content(content).handleColorTogglePage(colorToggleList, variables.pageDPIs)
        writeContent(variables.id!!, file.content, workDirectory)

        log.error("${this.javaClass.simpleName} executed")
    }

    companion object{
        private val log: Log = LogFactory.getLog(ColorToggleDelegate::class.java)
    }
}