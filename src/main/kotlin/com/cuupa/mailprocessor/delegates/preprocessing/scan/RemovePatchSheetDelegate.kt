package com.cuupa.mailprocessor.delegates.preprocessing.scan

import com.cuupa.mailprocessor.process.ProcessVariables
import com.cuupa.mailprocessor.services.files.content.FileFacade
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.engine.delegate.JavaDelegate

class RemovePatchSheetDelegate : JavaDelegate {

    override fun execute(execution: DelegateExecution?) {
        val variables = ProcessVariables(execution)
        val patchSheets = variables.patchSheets

        val file = FileFacade.content(variables.content).removePatchSheets(patchSheets)
        variables.content =file.content
    }
}