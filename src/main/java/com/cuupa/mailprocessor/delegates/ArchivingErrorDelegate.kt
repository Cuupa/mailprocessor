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
        if (handler.archived) {
            return
        }

        if (handler.isScanMail) {
            val scanProperties = configuration.getConfigurationForUser(handler.username).scanProperties
            scanService.moveScan(handler.fileName,
                                 scanProperties.path,
                                 scanProperties.port,
                                 handler.fileContent,
                                 scanProperties.errorFolder,
                                 scanProperties.username,
                                 scanProperties.password)
        }
        log.warn("Error archiving ${handler.fileName}")
    }

    companion object {
        private val log = LogFactory.getLog(ArchivingErrorDelegate::class.java)
    }

}
