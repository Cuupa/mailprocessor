package com.cuupa.mailprocessor.services

import com.cuupa.mailprocessor.process.CallActivityConstants
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import org.camunda.bpm.engine.RuntimeService

class MailprocessorStartService(private val runtimeService: RuntimeService) {

    fun startScanProcess(variables: Map<String, Any?>) {
        val finalVariables = generalCalls(variables)
        finalVariables[CallActivityConstants.PREPROCESSING_CALL] = CallActivityConstants.SCAN_PREPROCESSING
        start(finalVariables)
    }

    fun startEmailProcess(variables: Map<String, Any?>) {
        val finalVariables = generalCalls(variables)
        finalVariables[CallActivityConstants.PREPROCESSING_CALL] = CallActivityConstants.EMAIL_PREPROCESSING
        start(finalVariables)
    }

    private fun start(variables: Map<String, Any?>) {
        try {
            runtimeService.startProcessInstanceByKey(mailprocessorKey, variables)
        } catch (e: Exception) {
            log.error("Failed to start process with variables ${variables.entries.joinToString()}", e)
        }
    }

    private fun generalCalls(variables: Map<String, Any?>): MutableMap<String, Any?> {
        val generalVariables = variables.toMutableMap()
        generalVariables[CallActivityConstants.CONVERTING] = CallActivityConstants.CONVERTING
        generalVariables[CallActivityConstants.OCR] = CallActivityConstants.OCR
        generalVariables[CallActivityConstants.CLASSIFICATION] = CallActivityConstants.CLASSIFICATION
        generalVariables[CallActivityConstants.QA] = CallActivityConstants.QA
        generalVariables[CallActivityConstants.ARCHIVING] = CallActivityConstants.ARCHIVING
        return generalVariables
    }

    companion object {
        const val mailprocessorKey = "mailprocessor"

        val log: Log = LogFactory.getLog(MailprocessorStartService::class.java)
    }
}