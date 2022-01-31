package com.cuupa.mailprocessor.services

import com.cuupa.mailprocessor.delegates.preprocessing.scan.DPI
import com.cuupa.mailprocessor.delegates.preprocessing.scan.FileType
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.nio.file.Files
import java.nio.file.Paths

class BarcodeReaderTest {

    val document = "src/test/resources/patches_a4.pdf"

    val unitToTest = BarcodeReader()

    @Test
    fun shouldReadBarcodeFromPDF() {
        val bytes = Files.readAllBytes(Paths.get(document))

        unitToTest.readBarcode(bytes, FileType.PDF, listOf(DPI(300f, 300f)))
    }

}