package com.cuupa.mailprocessor.delegates

import com.cuupa.mailprocessor.MailprocessorConfiguration
import com.cuupa.mailprocessor.process.ProcessInstanceHandler
import com.cuupa.mailprocessor.services.input.email.EmailService
import com.cuupa.mailprocessor.services.input.scan.ScanService
import com.cuupa.mailprocessor.userconfiguration.EmailProperties
import com.cuupa.mailprocessor.userconfiguration.ScanProperties
import org.apache.juli.logging.LogFactory
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.engine.delegate.JavaDelegate

class ArchivingSuccessDelegate(private val scanService: ScanService, private val emailService: EmailService,
                               private val configuration: MailprocessorConfiguration) : JavaDelegate {

    override fun execute(delegateExecution: DelegateExecution) {
        val handler = ProcessInstanceHandler(delegateExecution)
        if (!handler.archived) {
            return
        }
        val configurationForUser = configuration.getConfigurationForUser(handler.username)
        if (handler.isScanMail) {
            processScanSuccess(configurationForUser.scanProperties, handler)
        } else {
            processMailSuccess(configurationForUser.emailProperties,handler)
        }
        log.warn("Successfully archived ${handler.fileName} to ${handler.archivedFilename}")
    }

    private fun processMailSuccess(emailProperties: EmailProperties, handler: ProcessInstanceHandler) {
        if (emailProperties.isMarkAsRead) {
            emailService.markMailAsRead(handler.emailSubject, handler.emailLabel, handler.receivedDate, emailProperties)
        }
    }

    private fun processScanSuccess(scanProperties: ScanProperties, handler: ProcessInstanceHandler) {
        val scanMoved = scanService.moveScan(handler.fileName,
                                             handler.fileContent,
                                             scanProperties,
                                             scanProperties.successFolder!!)

        if (!scanMoved) {
            log.error("Error moving document ${handler.fileName} to ${scanProperties.successFolder}")
        }
    }

    companion object {
        private val log = LogFactory.getLog(ArchivingSuccessDelegate::class.java)
    }

}
