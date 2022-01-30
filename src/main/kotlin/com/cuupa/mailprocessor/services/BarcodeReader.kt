package com.cuupa.mailprocessor.services

import com.cuupa.mailprocessor.delegates.preprocessing.scan.FileType
import com.google.zxing.BinaryBitmap
import com.google.zxing.MultiFormatReader
import com.google.zxing.NotFoundException
import com.google.zxing.RGBLuminanceSource
import com.google.zxing.common.HybridBinarizer
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.rendering.PDFRenderer
import java.awt.Color
import java.awt.image.BufferedImage


class BarcodeReader {

    fun readBarcode(content: ByteArray?, type: FileType): List<BarcodeResult> {
        val pages: List<BufferedImage> = if (type == FileType.PDF) {
            getImagesFromPDF(content)
        } else {
            listOf(BufferedImage(0, 0, 0))
        }

        val barcodes = mutableListOf<BarcodeResult>()

        for (i in pages.indices) {
            val page = pages[i]
            try {
                val barcodeContent = MultiFormatReader().decode(convertImageToBinaryBitmap(page))
                barcodes.add(BarcodeResult(i, barcodeContent))
            } catch (ne: NotFoundException) {
                // simply no barcode found
            }
        }
        return barcodes
    }

    private fun convertImageToBinaryBitmap(image: BufferedImage): BinaryBitmap? {
        val pixels = image.getRGB(
            0, 0,
            image.width, image.height,
            null, 0, image.width
        )
        val source = RGBLuminanceSource(
            image.width,
            image.height,
            pixels
        )
        return BinaryBitmap(HybridBinarizer(source))
    }

    private fun getImagesFromPDF(content: ByteArray?): List<BufferedImage> {
        PDDocument.load(content).use { document ->
            val renderer = PDFRenderer(document)
            val pageImages = mutableListOf<BufferedImage>()
            for (index in 0 until document.numberOfPages) {
                pageImages.add(renderer.renderImageWithDPI(index, 300F))
            }
            return pageImages
        }
    }

    /**
     * got from
     * https://gist.github.com/boolean444/899d6fa2b18f6b8f0f56bf98ad15d5a8
     */
    private fun greyScale(img: BufferedImage): BufferedImage {
        for (i in 0 until img.height) {
            for (j in 0 until img.width) {
                val r = Color(img.getRGB(j, i)).red
                val g = Color(img.getRGB(j, i)).green
                val b = Color(img.getRGB(j, i)).blue
                val grayScaled = (r + g + b) / 3
                img.setRGB(j, i, Color(grayScaled, grayScaled, grayScaled).rgb)
            }
        }
        return img
    }
}
