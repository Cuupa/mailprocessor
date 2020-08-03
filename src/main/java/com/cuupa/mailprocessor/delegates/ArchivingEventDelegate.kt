package com.cuupa.mailprocessor.delegates

import com.cuupa.mailprocessor.process.ProcessInstanceHandler
import com.cuupa.mailprocessor.process.ProcessProperty
import com.cuupa.mailprocessor.services.input.scan.ScanService
import com.cuupa.mailprocessor.userconfiguration.ScanProperties
import org.apache.juli.logging.LogFactory
import org.camunda.bpm.engine.RuntimeService
import org.camunda.bpm.engine.delegate.JavaDelegate
import org.camunda.bpm.engine.runtime.ProcessInstance

abstract class ArchivingEventDelegate(private val scanService: ScanService,
                                      private val runtimeService: RuntimeService) : JavaDelegate {

    private fun moveScan(filename: String, content: ByteArray, target: String, scanProperties: ScanProperties) {
        val scanMoved = scanService.moveScan(filename, content, target, scanProperties)

        if (!scanMoved) {
            log.error("Error moving document $filename to $target")
        }
    }

    private fun getRunningProcessInstances(originalFilename: String, mailType: String): List<ProcessInstance> {
        return runtimeService.createProcessInstanceQuery()
            .active()
            .variableValueEquals(ProcessProperty.MAIL_TYPE.name, mailType)
            .variableValueEquals(ProcessProperty.ORIGINAL_FILENAME.name, originalFilename)
            .list()?: listOf()
    }

    protected fun processZip(target: String, scanProperties: ScanProperties, handler: ProcessInstanceHandler) {
        val list = getRunningProcessInstances(handler.originalFilename, "zip")

        when {
            list.isEmpty() -> {
                moveScan(handler.fileName!!, handler.fileContent, target, scanProperties)
                moveScan(handler.originalFilename, handler.fileContent, target, scanProperties)
            }
            else -> moveScan(handler.fileName!!, handler.fileContent, target, scanProperties)
        }
    }

    protected fun processScan(target: String, scanProperties: ScanProperties, handler: ProcessInstanceHandler) {
        moveScan(handler.fileName!!, handler.fileContent, target, scanProperties)
    }

    companion object {
        private val log = LogFactory.getLog(ArchivingEventDelegate::class.java)
    }
}