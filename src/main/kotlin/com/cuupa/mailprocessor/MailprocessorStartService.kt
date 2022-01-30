package com.cuupa.mailprocessor

import com.cuupa.mailprocessor.process.ModelConstants
import org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import org.camunda.bpm.engine.RuntimeService

class MailprocessorStartService(private val runtimeService: RuntimeService) {

    fun startScanProcess(variables: Map<String, Any?>) {
        val finalVariables = generalCalls(variables)
        finalVariables[ModelConstants.PREPROCESSING] = ModelConstants.SCAN_PREPROCESSING
        start(finalVariables)
    }

    fun startEmailProcess(variables: Map<String, Any?>) {
        val finalVariables = generalCalls(variables)
        finalVariables[ModelConstants.PREPROCESSING] = ModelConstants.EMAIL_PREPROCESSING
        start(finalVariables)
    }

    private fun start(variables: Map<String, Any?>) {
        try {
            runtimeService.startProcessInstanceByKey(mailprocessorKey, variables)
        } catch (e: Exception) {
            log.error("Failed to start process with variables ${reflectionToString(variables)}", e)
        }
    }

    private fun generalCalls(variables: Map<String, Any?>): MutableMap<String, Any?> {
        val generalVariables = variables.toMutableMap()
        generalVariables[ModelConstants.CONVERTING] = ModelConstants.CONVERTING
        generalVariables[ModelConstants.OCR] = ModelConstants.OCR
        generalVariables[ModelConstants.CLASSIFICATION] = ModelConstants.CLASSIFICATION
        generalVariables[ModelConstants.QA] = ModelConstants.QA
        generalVariables[ModelConstants.ARCHIVING] = ModelConstants.ARCHIVING
        return generalVariables
    }

    companion object {
        const val mailprocessorKey = "mailprocessor"

        val log: Log = LogFactory.getLog(MailprocessorStartService::class.java)
    }
}