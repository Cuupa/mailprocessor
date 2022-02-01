package com.cuupa.mailprocessor.services

import com.cuupa.mailprocessor.services.files.content.FileFacade
import com.cuupa.mailprocessor.services.files.util.DPI
import com.google.zxing.BinaryBitmap
import com.google.zxing.MultiFormatReader
import com.google.zxing.NotFoundException
import com.google.zxing.RGBLuminanceSource
import com.google.zxing.common.HybridBinarizer
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.rendering.PDFRenderer
import org.springframework.http.MediaType
import java.awt.image.BufferedImage


class BarcodeReader {

    fun readBarcode(content: ByteArray?, type: MediaType, dpi: List<DPI>): List<BarcodeResult> {

        val pages = FileFacade.content(content).getImages(dpi)
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
}
