package com.cuupa.mailprocessor.services.files.content

import com.cuupa.mailprocessor.services.BarcodeResult
import com.cuupa.mailprocessor.services.Extensions.getText
import com.cuupa.mailprocessor.services.files.util.DPI
import com.cuupa.mailprocessor.services.files.util.DpiService
import com.cuupa.mailprocessor.services.files.util.PageImage
import org.apache.pdfbox.cos.COSDictionary
import org.apache.pdfbox.cos.COSName
import org.apache.pdfbox.multipdf.PDFMergerUtility
import org.apache.pdfbox.multipdf.Splitter
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPage
import org.apache.pdfbox.pdmodel.PDPageContentStream
import org.apache.pdfbox.pdmodel.common.PDStream
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory
import org.apache.pdfbox.rendering.PDFRenderer
import org.apache.pdfbox.tools.PDFMerger
import java.awt.color.ColorSpace
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

class Pdf(content: ByteArray) : FileContent(content) {

    override fun embeddMetadata(metadata: List<String>): FileContent {
        PDDocument.load(content).use { document ->
            document.documentInformation.keywords = metadata.joinToString()
            document.isAllSecurityToBeRemoved = true
            content = saveChanges(document)
        }
        return this
    }

    override fun getText(): List<String> {
        PDDocument.load(content).use { document ->
            return document.getText()
        }
    }

    override fun getImages(dpi: List<DPI>): List<BufferedImage> {
        PDDocument.load(content).use { document ->
            val renderer = PDFRenderer(document)
            val pageImages = mutableListOf<BufferedImage>()
            for (index in 0 until document.numberOfPages) {
                pageImages.add(renderer.renderImageWithDPI(index, dpi[index].x))
            }
            return pageImages
        }
    }

    override fun handleColorTogglePage(colorToggleList: List<Int>, pageDPIs: List<DPI>): FileContent {
        PDDocument.load(content).use { document ->
            // it is not really possible to convert a greyscale image to color,
            // so let's just handle images with greyscale as the target color scheme
            val pagesToConvert = getPageColorProfile(document, colorToggleList, pageDPIs)
                .filter { it.key == ColorSpace.CS_GRAY }
                .flatMap { it.value }

            applyGreyScale(pagesToConvert).forEach {
                val (oldPage, newPage) = createPage(document, it)
                document.pages.insertAfter(newPage, oldPage)
                document.pages.remove(oldPage)
            }

            colorToggleList.forEach {
                document.pages.remove(document.getPage(it))
            }

            content = saveChanges(document)
        }
        return this
    }

    override fun handleFileSeparationPatchSheet(pageSeparationSheets: List<Int>): List<ByteArray> {

        if (pageSeparationSheets.isEmpty()) {
            return listOf(content)
        }

        val result = mutableListOf<ByteArray>()

        PDDocument.load(content).use { document ->
            var startPage = 1
            var endpage: Int

            for (index in pageSeparationSheets.indices) {
                endpage = pageSeparationSheets[index]
                val splitted = Splitter().apply {
                    setStartPage(startPage)
                    setEndPage(endpage)
                    setSplitAtPage(endpage)
                }.split(document)
                // Save the first document.
                // The last document might have further page separation sheets,
                // so we discard these and iterate over the parent document to find
                // the next separation sheet
                val splitresult = splitted.first()
                result.add(saveChanges(splitresult))
                // the page is 1 based, the page separationsheets are 0 based
                startPage = if (index + 1 < pageSeparationSheets.size) {
                    pageSeparationSheets[index] + 2
                } else {
                    // +2 because we want to skipp the patch sheet
                    endpage + 2
                }
            }

            // if we have 2 page separation sheets, we need to have 3 documents
            // as a result and therefore it will not be covered by the loop above.
            // So we use the splitter one more time, starting from the index of the last
            // separation sheet + 1, ending at the last page of the document
            // The result should be a list with one element
            val doc = Splitter().apply {
                setStartPage(startPage)
                setEndPage(document.numberOfPages)
                setSplitAtPage(document.numberOfPages)
            }.split(document).first()
            result.add(saveChanges(doc))
        }
        return result
    }

    override fun getDPIPerPage(): List<DPI> {
        PDDocument.load(content).use { document ->
            return document.pages.map { page ->
                DpiService(page).determineDpi()
            }
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

    private fun toggle(color: Int?): Int {
        return when (color) {
            null -> ColorSpace.CS_sRGB
            ColorSpace.CS_GRAY -> ColorSpace.CS_sRGB
            else -> ColorSpace.CS_GRAY
        }
    }

    private fun getPageColorProfile(
        document: PDDocument, colorToggleList: List<Int>, pageDPIs: List<DPI>
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
            val renderedPage = renderer.renderImageWithDPI(index, pageDPIs[index].x)
            pageImages[color]?.add(PageImage(index, renderedPage))
        }

        return pageImages
    }

    private fun saveChanges(it: PDDocument): ByteArray {
        val outputStream = ByteArrayOutputStream()
        it.save(outputStream)
        return outputStream.toByteArray()
    }
}
