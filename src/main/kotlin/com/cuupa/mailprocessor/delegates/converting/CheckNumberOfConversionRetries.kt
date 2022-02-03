package com.cuupa.mailprocessor.delegates.converting

import com.cuupa.mailprocessor.delegates.ocr.CheckOCRResultWaitTimeDelegate
import com.cuupa.mailprocessor.process.ProcessVariables
import org.camunda.bpm.engine.delegate.BpmnError
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.engine.delegate.JavaDelegate

class CheckNumberOfConversionRetries : JavaDelegate {

    override fun execute(execution: DelegateExecution?) {
        val variables = ProcessVariables(execution)
        val attempts = variables.numberOfConvertingAttempts
        if(attempts < CheckOCRResultWaitTimeDelegate.maxWait) {
            variables.numberOfConvertingAttempts = attempts + 1
        } else {
            throw BpmnError("Max number of retries reached")
        }
    }
}
