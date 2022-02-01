package com.cuupa.mailprocessor.services

import com.cuupa.mailprocessor.services.files.util.DPI
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import java.nio.file.Files
import java.nio.file.Paths

class BarcodeReaderTest {

    val document = "src/test/resources/patches_a4.pdf"

    val dpiList = listOf(
        DPI(300f, 300f),
        DPI(300f, 300f),
        DPI(300f, 300f),
        DPI(300f, 300f),
        DPI(300f, 300f),
        DPI(300f, 300f)
    )

    val unitToTest = BarcodeReader()

    @Test
    fun shouldReadBarcodeFromPDF() {
        val bytes = Files.readAllBytes(Paths.get(document))

        unitToTest.readBarcode(bytes, MediaType.APPLICATION_PDF, dpiList)
    }
}