package com.cuupa.mailprocessor.delegates.ocr

import com.cuupa.mailprocessor.process.ProcessVariables
import org.camunda.bpm.engine.delegate.BpmnError
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.engine.delegate.JavaDelegate

/**
 * @author Simon Thiel (https://github.com/cuupa)
 */
class CheckOCRResultWaitTimeDelegate: JavaDelegate {

    override fun execute(execution: DelegateExecution?) {
        val variables = ProcessVariables(execution)
        val attempts = variables.numberOfOcrAttempts
        if(attempts < maxWait) {
            variables.numberOfOcrAttempts = attempts + 1
        } else {
            throw BpmnError("Max number of tries reached")
        }
    }

    companion object {
        const val maxWait = 5
    }
}