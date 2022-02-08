package com.cuupa.mailprocessor.delegates.preprocessing.scan

import com.cuupa.mailprocessor.delegates.AbstractJavaDelegate
import com.cuupa.mailprocessor.process.ProcessVariables
import org.camunda.bpm.engine.delegate.DelegateExecution

class SetDocumentsDelegate : AbstractJavaDelegate() {

    override fun execute(execution: DelegateExecution?) {
        val variables = ProcessVariables(execution)
        variables.documents = listOf(variables.id!!)
    }
}