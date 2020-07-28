package com.cuupa.mailprocessor.delegates

import com.cuupa.mailprocessor.MailprocessorConfiguration
import com.cuupa.mailprocessor.process.ProcessInstanceHandler
import com.cuupa.mailprocessor.services.input.email.EmailService
import com.cuupa.mailprocessor.services.input.scan.ScanService
import org.apache.juli.logging.LogFactory
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.engine.delegate.JavaDelegate

class ArchivingSuccessDelegate(private val scanService: ScanService,
                               private val emailService: EmailService,
                               private val configuration: MailprocessorConfiguration) : JavaDelegate {

    override fun execute(delegateExecution: DelegateExecution) {
        val handler = ProcessInstanceHandler(delegateExecution)
        if (!handler.archived) {
            return
        }
        val configurationForUser = configuration.getConfigurationForUser(handler.username)
        if (handler.isScanMail) {
            val scanProperties = configurationForUser.scanProperties
            scanService.moveScan(handler.fileName,
                                 scanProperties.path,
                                 scanProperties.port,
                                 handler.fileContent,
                                 scanProperties.successFolder,
                                 scanProperties.username,
                                 scanProperties.password)
        } else {
            emailService.markMailAsRead(handler.emailSubject,
                                        handler.emailLabel,
                                        handler.receivedDate,
                                        configurationForUser.emailProperties)
        }
        log.warn("Successfully archived ${handler.fileName} to ${handler.archivedFilename}")
    }

    companion object {
        private val log = LogFactory.getLog(ArchivingSuccessDelegate::class.java)
    }

}
