package com.cuupa.mailprocessor.delegates.preprocessing.scan

import com.cuupa.mailprocessor.process.ProcessVariables
import com.cuupa.mailprocessor.services.files.content.FileFacade
import com.cuupa.mailprocessor.services.files.util.DpiService
import org.apache.pdfbox.pdmodel.PDDocument
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.engine.delegate.JavaDelegate


class DetectDpiDelegate : JavaDelegate {

    override fun execute(execution: DelegateExecution?) {
        val variables = ProcessVariables(execution)

        FileFacade.content(variables.content).getDPIPerPage()
    }
}