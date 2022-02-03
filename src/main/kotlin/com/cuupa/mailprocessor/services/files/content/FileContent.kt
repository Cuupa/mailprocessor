package com.cuupa.mailprocessor.services.files.content

import com.cuupa.mailprocessor.services.BarcodeResult
import com.cuupa.mailprocessor.services.files.util.DPI
import com.cuupa.mailprocessor.services.files.util.PageImage
import java.awt.color.ColorSpace
import java.awt.image.BufferedImage
import java.awt.image.ColorConvertOp

abstract class FileContent(var content: ByteArray) {

    abstract fun embeddMetadata(metadata: List<String>): FileContent

    abstract fun getText(): List<String>

    abstract fun handleColorTogglePage(colorToggleList: List<Int>, pageDPIs: List<DPI>): FileContent

    abstract fun handleFileSeparationPatchSheet(pageSeparationSheets: List<Int>): List<ByteArray>

    abstract fun getImages(dpi: List<DPI>): List<BufferedImage>

    abstract fun getDPIPerPage(): List<DPI>

    protected fun applyGreyScale(pagesToConvert: List<PageImage>): List<PageImage> {
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
}
