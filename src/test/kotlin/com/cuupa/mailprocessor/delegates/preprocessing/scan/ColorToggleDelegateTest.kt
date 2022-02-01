package com.cuupa.mailprocessor.delegates.preprocessing.scan

import com.cuupa.mailprocessor.process.ProcessProperty
import com.cuupa.mailprocessor.services.Barcode
import com.cuupa.mailprocessor.services.BarcodeResult
import com.cuupa.mailprocessor.services.files.util.DPI
import com.google.zxing.BarcodeFormat
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.engine.impl.pvm.runtime.ExecutionImpl
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import java.nio.file.Files
import java.nio.file.Paths

class ColorToggleDelegateTest {

    lateinit var execution: DelegateExecution

    val input =
        "path/to/file.pdf"
    val output =
        "path/to/result.pdf"

    private val unitToTest = ColorToggleDelegate()

    @BeforeEach
    fun setUp() {

        val patchSheets = listOf(
            BarcodeResult(1, Barcode(BarcodeFormat.CODE_39, "PATCH4"))
        )
        val content = Files.readAllBytes(Paths.get(input))
        execution = ExecutionImpl()
        execution.setVariable(ProcessProperty.FILE_CONTENT.name, content)
        execution.setVariable(ProcessProperty.PATCH_SHEETS.name, patchSheets)
        execution.setVariable(ProcessProperty.DPI_PER_PAGE.name, listOf(
            DPI(300F, 300F),
            DPI(300F, 300F),
            DPI(300F, 300F),
            DPI(300F, 300F),
            DPI(300F, 300F),
            DPI(300F, 300F)
        ))
    }

    //@Test
    fun shouldReplacePage() {
        val contentBefore = execution.getVariable(ProcessProperty.FILE_CONTENT.name)
        unitToTest.execute(execution)
        val contentAfter = execution.getVariable(ProcessProperty.FILE_CONTENT.name)

        Files.write(Paths.get(output), (execution.variables[ProcessProperty.FILE_CONTENT.name] as ByteArray))

        assertNotEquals(contentBefore, contentAfter)
    }

}