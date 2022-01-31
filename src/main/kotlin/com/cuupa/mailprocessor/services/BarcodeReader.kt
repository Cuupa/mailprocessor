package com.cuupa.mailprocessor.services

import com.cuupa.mailprocessor.delegates.preprocessing.scan.DPI
import com.cuupa.mailprocessor.delegates.preprocessing.scan.FileType
import com.google.zxing.BinaryBitmap
import com.google.zxing.MultiFormatReader
import com.google.zxing.NotFoundException
import com.google.zxing.RGBLuminanceSource
import com.google.zxing.common.HybridBinarizer
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.rendering.PDFRenderer
import java.awt.image.BufferedImage


class BarcodeReader {

    fun readBarcode(content: ByteArray?, type: FileType, dpi: List<DPI>): List<BarcodeResult> {
        val pages: List<BufferedImage> = if (type == FileType.PDF) {
            getImagesFromPDF(content, dpi)
        } else {
            listOf(BufferedImage(0, 0, 0))
        }

        val barcodes = mutableListOf<BarcodeResult>()

        for (i in pages.indices) {
            val page = pages[i]
            try {
                val barcodeContent = MultiFormatReader().decode(convertImageToBinaryBitmap(page))
                barcodes.add(BarcodeResult(i, Barcode(barcodeContent.barcodeFormat, barcodeContent.text)))
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

    private fun getImagesFromPDF(content: ByteArray?, dpi: List<DPI>): List<BufferedImage> {
        PDDocument.load(content).use { document ->
            val renderer = PDFRenderer(document)
            val pageImages = mutableListOf<BufferedImage>()
            for (index in 0 until document.numberOfPages) {
                pageImages.add(renderer.renderImageWithDPI(index, dpi[index].x))
            }
            return pageImages
        }
    }
}
