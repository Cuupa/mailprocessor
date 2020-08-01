package com.cuupa.mailprocessor.delegates

import com.cuupa.mailprocessor.MailprocessorConfiguration
import com.cuupa.mailprocessor.process.ProcessInstanceHandler
import com.cuupa.mailprocessor.services.input.scan.ScanService
import org.apache.juli.logging.LogFactory
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.engine.delegate.JavaDelegate

class ArchivingErrorDelegate(private val scanService: ScanService,
                             private val configuration: MailprocessorConfiguration) : JavaDelegate {

    override fun execute(delegateExecution: DelegateExecution) {
        val handler = ProcessInstanceHandler(delegateExecution)
        when {
            handler.archived -> return
            handler.isScanMail -> processScanError(handler)
        }
        log.warn(handler.errors.joinToString("\n", "", ""))
        log.warn("Error archiving ${handler.fileName}")
    }

    private fun processScanError(handler: ProcessInstanceHandler) {
        val scanProperties = configuration.getConfigurationForUser(handler.username).scanProperties

        val filename = when {
            handler.isZipFile -> handler.originalFilename
            else -> handler.fileName
        }

        val scanMoved = scanService.moveScan(filename,
                                             handler.fileContent,
                                             scanProperties,
                                             scanProperties.errorFolder!!)

        if (!scanMoved && !handler.isZipFile) {
            log.error("Error moving document ${handler.fileName} to ${scanProperties.errorFolder}")
        }
    }

    companion object {
        private val log = LogFactory.getLog(ArchivingErrorDelegate::class.java)
    }

}
