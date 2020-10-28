package com.cuupa.mailprocessor.delegates

import com.cuupa.mailprocessor.MailprocessorConfiguration
import com.cuupa.mailprocessor.process.ProcessInstanceHandler
import com.cuupa.mailprocessor.services.input.email.EmailService
import com.cuupa.mailprocessor.services.input.scan.ScanService
import com.cuupa.mailprocessor.userconfiguration.EmailProperties
import org.apache.juli.logging.LogFactory
import org.camunda.bpm.engine.RuntimeService
import org.camunda.bpm.engine.delegate.DelegateExecution

class ArchivingSuccessDelegate(scanService: ScanService, private val emailService: EmailService,
                               runtimeService: RuntimeService, private val configuration: MailprocessorConfiguration) :
        ArchivingEventDelegate(scanService, runtimeService) {

    override fun execute(delegateExecution: DelegateExecution) {
        val handler = ProcessInstanceHandler(delegateExecution)
        val configurationForUser = configuration.getConfigurationForUser(handler.username)
        val scanProperties = configurationForUser.scanProperties
        when {
            handler.isScanMail -> processScan(scanProperties.successFolder, scanProperties, handler)
            handler.isZipFile -> processZip(scanProperties.successFolder, scanProperties, handler)
            else -> processMailSuccess(configurationForUser.emailProperties, handler)
        }
        if (log.isWarnEnabled) {
            log.warn("Successfully archived ${handler.fileName} to ${handler.archivedFilename}")
        }
    }

    private fun processMailSuccess(emailProperties: EmailProperties, handler: ProcessInstanceHandler) {
        if (emailProperties.isMarkAsRead) {
            emailService.markMailAsRead(handler.emailSubject, handler.emailLabel, handler.receivedDate, emailProperties)
        }
    }

    companion object {
        private val log = LogFactory.getLog(ArchivingSuccessDelegate::class.java)
    }
}
