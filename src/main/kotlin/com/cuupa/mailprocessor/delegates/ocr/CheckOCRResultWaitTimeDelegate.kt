package com.cuupa.mailprocessor.delegates.ocr

import com.cuupa.mailprocessor.delegates.preprocessing.scan.ColorToggleDelegate
import com.cuupa.mailprocessor.process.ProcessVariables
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
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
            throw BpmnError("Max number of retries reached")
        }

        log.error("${this.javaClass.simpleName} executed")
    }

    companion object {
        const val maxWait = 5
        private val log: Log = LogFactory.getLog(ColorToggleDelegate::class.java)
    }
}