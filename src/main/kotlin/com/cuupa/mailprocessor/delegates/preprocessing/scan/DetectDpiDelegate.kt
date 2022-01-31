package com.cuupa.mailprocessor.delegates.preprocessing.scan

import com.cuupa.mailprocessor.process.ProcessVariables
import com.cuupa.mailprocessor.services.files.util.DpiService
import org.apache.pdfbox.pdmodel.PDDocument
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.engine.delegate.JavaDelegate


class DetectDpiDelegate : JavaDelegate {

    override fun execute(execution: DelegateExecution?) {
        val variables = ProcessVariables(execution)

        PDDocument.load(variables.content).use { document ->
            variables.pageDPIs = document.pages.map { page ->
                DpiService(page).determineDpi()
            }
        }
    }
}