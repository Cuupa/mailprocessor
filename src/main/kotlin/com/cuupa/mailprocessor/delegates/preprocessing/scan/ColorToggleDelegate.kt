package com.cuupa.mailprocessor.delegates.preprocessing.scan

import com.cuupa.mailprocessor.process.ProcessVariables
import com.cuupa.mailprocessor.services.BarcodeResult
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

        PDDocument.load(variables.content).use { document ->
            val colorToggleProfiles = getPageColorProfile(document, colorToggleList, variables)

            // it is not really possible to convert a greyscale image to color,
            // so let's just handle images with greyscale as the target color scheme
            val pagesToConvert = colorToggleProfiles
                .filter { it.key == ColorSpace.CS_GRAY }
                .flatMap { it.value }

            val converted = convert(pagesToConvert)

            converted.forEach {
                val (oldPage, newPage) = createPage(document, it)
                document.pages.insertAfter(newPage, oldPage)
                document.pages.remove(oldPage)
            }

            val outputStream = ByteArrayOutputStream()
            document.save(outputStream)
            document.close()
            variables.content = outputStream.toByteArray()
        }
    }

    private fun createPage(
        document: PDDocument,
        it: PageImage
    ): Pair<PDPage, PDPage> {
        val image = LosslessFactory.createFromImage(document, it.image)

        val oldPage = document.getPage(it.pageIndex)
        val newPage = copy(oldPage, document)

        PDPageContentStream(document, newPage).use { contentStream ->
            val width = oldPage.mediaBox.width
            val height = oldPage.mediaBox.height
            contentStream.drawImage(image, 0f, 0f, width, height)
        }
        return Pair(oldPage, newPage)
    }

    private fun copy(oldPage: PDPage, document: PDDocument): PDPage {
        val dest = PDStream(document, oldPage.contents, COSName.FLATE_DECODE)
        return PDPage(COSDictionary(oldPage.cosObject)).apply {
            setContents(dest)
            cropBox = oldPage.cropBox
            mediaBox = oldPage.mediaBox
            rotation = oldPage.rotation
        }
    }

    private fun convert(pagesToConvert: List<PageImage>): List<PageImage> {
        val convertedPages = mutableListOf<PageImage>()

        for (sourceImage in pagesToConvert) {
            val width: Int = sourceImage.image.width
            val height: Int = sourceImage.image.height

            val image = BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY)
            image.createGraphics().drawImage(sourceImage.image, 0, 0, width, height, null)

            val grayColorSpace = ColorSpace.getInstance(ColorSpace.CS_GRAY)
            val op = ColorConvertOp(grayColorSpace, image.colorModel.colorSpace, null)
            op.filter(image, image)
            convertedPages.add(PageImage(sourceImage.pageIndex, image))
        }

        return convertedPages
    }

    private fun getPageColorProfile(
        document: PDDocument, colorToggleList: List<Int>, variables: ProcessVariables
    ): Map<Int, List<PageImage>> {
        val pageImages = mutableMapOf<Int, MutableList<PageImage>>()
        val renderer = PDFRenderer(document)

        var color = renderer.renderImage(0).colorModel.colorSpace.type

        for (index in 0 until document.numberOfPages) {
            if (colorToggleList.contains(index)) {
                color = toggle(color)
            }
            if (!pageImages.contains(color)) {
                pageImages[color] = mutableListOf()
            }
            val renderedPage = renderer.renderImageWithDPI(index, variables.pageDPIs[index].x)
            pageImages[color]?.add(PageImage(index, renderedPage))
        }

        return pageImages
    }

    private fun toggle(color: Int?): Int {
        return when (color) {
            null -> ColorSpace.CS_sRGB
            ColorSpace.CS_GRAY -> ColorSpace.CS_sRGB
            else -> ColorSpace.CS_GRAY
        }
    }

    private fun isColorToggle(it: BarcodeResult) =
        it.barcode.barcodeFormat == BarcodeFormat.CODE_39 && it.barcode.text == colorToggleCode

    companion object {
        const val colorToggleCode = "PATCH4"
    }
}