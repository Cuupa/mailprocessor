package com.cuupa.mailprocessor.delegates.classification

import com.cuupa.mailprocessor.delegates.AbstractJavaDelegate
import com.cuupa.mailprocessor.process.ProcessVariables
import com.cuupa.mailprocessor.services.files.content.FileFacade
import com.cuupa.mailprocessor.userconfiguration.WorkDirectory
import org.camunda.bpm.engine.delegate.DelegateExecution

class EmbeddTopicsDelegate(private val workConfig: WorkDirectory) : AbstractJavaDelegate() {

    override fun execute(execution: DelegateExecution?) {
        val variables = ProcessVariables(execution)
        val content = getContent(variables.id!!, workConfig)
        val pdf = FileFacade.content(content).embeddMetadata(variables.topics)
        writeContent(variables.id!!, pdf.content, workConfig)
    }
}