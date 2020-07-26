package com.cuupa.mailprocessor.delegates

import com.cuupa.mailprocessor.process.ProcessInstanceHandler
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.engine.delegate.JavaDelegate

class DefaultValueDelegate : JavaDelegate {

    override fun execute(delegateExecution: DelegateExecution?) {
        val handler = ProcessInstanceHandler(delegateExecution)
        handler.sender = "UNKNOWN"
        handler.topics = listOf("OTHER")
    }
}
