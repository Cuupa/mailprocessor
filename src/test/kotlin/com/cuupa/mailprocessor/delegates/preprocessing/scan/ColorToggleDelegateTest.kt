package com.cuupa.mailprocessor.delegates.preprocessing.scan

import com.cuupa.mailprocessor.process.ProcessProperty
import com.cuupa.mailprocessor.services.Barcode
import com.cuupa.mailprocessor.services.BarcodeResult
import com.cuupa.mailprocessor.services.files.util.DPI
import com.cuupa.mailprocessor.userconfiguration.WorkDirectory
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

    val id = "ABCDEF"

    private val unitToTest = ColorToggleDelegate(WorkDirectory().apply {
        path = input
    })

    @BeforeEach
    fun setUp() {

        val patchSheets = listOf(
            BarcodeResult(1, Barcode(BarcodeFormat.CODE_39, "PATCH4"))
        )
        execution = ExecutionImpl()
        execution.setVariable(ProcessProperty.ID.name, id)
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
        val contentBefore = Files.readAllBytes(Paths.get("${execution.getVariable(ProcessProperty.ID.name)}"))
        unitToTest.execute(execution)
        val contentAfter = Files.readAllBytes(Paths.get("${execution.getVariable(ProcessProperty.ID.name)}"))
        assertNotEquals(contentBefore, contentAfter)
    }

}