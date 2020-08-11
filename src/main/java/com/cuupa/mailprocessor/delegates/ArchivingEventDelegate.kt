package com.cuupa.mailprocessor.delegates

import com.cuupa.mailprocessor.process.ProcessInstanceHandler
import com.cuupa.mailprocessor.services.files.FileFactory
import com.cuupa.mailprocessor.services.input.scan.ScanService
import com.cuupa.mailprocessor.userconfiguration.ScanProperties
import org.apache.juli.logging.LogFactory
import org.camunda.bpm.engine.RuntimeService
import org.camunda.bpm.engine.delegate.JavaDelegate

abstract class ArchivingEventDelegate(private val scanService: ScanService,
                                      private val runtimeService: RuntimeService) : JavaDelegate {

    private fun moveScan(filename: String, content: ByteArray, target: String, scanProperties: ScanProperties) {
        val scanMoved = scanService.moveScan(filename, content, target, scanProperties)

        if (!scanMoved) {
            log.error("Error moving document $filename to $target")
        }
    }

    private fun moveScanIfExists(filename: String, content: ByteArray, target: String, scanProperties: ScanProperties) {
        FileFactory.getForPath(scanProperties.root!!).use { file ->
            file.init(scanProperties.username, scanProperties.password)
            if (file.exists("${scanProperties.root}/${scanProperties.workFolder}", filename)) {
                moveScan(filename, content, target, scanProperties)
            }
        }
    }

    protected fun processZip(target: String, scanProperties: ScanProperties, handler: ProcessInstanceHandler) {
        moveScan(handler.fileName, handler.fileContent, target, scanProperties)
        moveScanIfExists(handler.zipFileName, handler.fileContent, "${scanProperties.workFolder}/zip", scanProperties)
    }

    protected fun processScan(target: String, scanProperties: ScanProperties, handler: ProcessInstanceHandler) {
        moveScan(handler.fileName, handler.fileContent, target, scanProperties)
    }

    companion object {
        private val log = LogFactory.getLog(ArchivingEventDelegate::class.java)
    }
}