package com.cuupa.mailprocessor.delegates.ocr

import com.cuupa.mailprocessor.process.ProcessVariables
import com.cuupa.mailprocessor.services.files.content.FileFacade
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.engine.delegate.JavaDelegate

class ExtractTextDelegate : JavaDelegate {

    override fun execute(execution: DelegateExecution?) {
        val variables = ProcessVariables(execution)
        variables.plaintext = FileFacade.content(variables.content).getText()
    }
}