package com.cuupa.mailprocessor.services.files.content

import com.cuupa.mailprocessor.services.files.util.DPI
import java.awt.image.BufferedImage

class Jpg(content: ByteArray) : FileContent(content) {
    override fun embeddMetadata(metadata: List<String>): FileContent {
        TODO("Not yet implemented")
    }

    override fun getText(): List<String> {
        TODO("Not yet implemented")
    }

    override fun handleColorTogglePage(colorToggleList: List<Int>, pageDPIs: List<DPI>): FileContent {
        TODO("Not yet implemented")
    }

    override fun handleFileSeparationPatchSheet(pageSeparationSheets: List<Int>): List<ByteArray> {
        TODO("Not yet implemented")
    }

    override fun getImages(dpi: List<DPI>): List<BufferedImage> {
        TODO("Not yet implemented")
    }

    override fun getDPIPerPage(): List<DPI> {
        TODO("Not yet implemented")
    }

}
