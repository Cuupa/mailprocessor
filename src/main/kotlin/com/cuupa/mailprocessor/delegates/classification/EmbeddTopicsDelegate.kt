package com.cuupa.mailprocessor.delegates.classification

import com.cuupa.mailprocessor.process.ProcessVariables
import com.cuupa.mailprocessor.services.files.content.FileFacade
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.engine.delegate.JavaDelegate

class EmbeddTopicsDelegate : JavaDelegate {

    override fun execute(execution: DelegateExecution?) {
        val variables = ProcessVariables(execution)
        val pdf = FileFacade.content(variables.content).embeddMetadata(variables.topics)
        variables.content = pdf.content
    }
}