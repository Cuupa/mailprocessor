package com.cuupa.mailprocessor.services.files.content

import org.apache.pdfbox.pdmodel.PDDocument
import org.junit.jupiter.api.Test

import java.nio.file.Files
import java.nio.file.Paths
import kotlin.test.assertEquals

class PageSeparationTest {

    private val testDocument = "src/test/resources/page_separation_document.pdf"

    private val pageSeparationSheets = listOf(1, 4)

    @Test
    fun handleFileSeparationPatchSheet() {
        val content = Files.readAllBytes(Paths.get(testDocument))
        val documents = FileFacade.content(content)
            .handleFileSeparationPatchSheet(pageSeparationSheets)

        assertEquals(3, documents.size)

        PDDocument.load(documents[0]).use {
            assertEquals(1, it.numberOfPages)
        }

        PDDocument.load(documents[1]).use {
            assertEquals(2, it.numberOfPages)
        }

        PDDocument.load(documents[2]).use {
            assertEquals(1, it.numberOfPages)
        }
    }
}