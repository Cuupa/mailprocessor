package com.cuupa.mailprocessor.services

import com.cuupa.mailprocessor.MailprocessorConfiguration
import com.cuupa.mailprocessor.process.ProcessProperty
import com.cuupa.mailprocessor.services.input.Document
import com.cuupa.mailprocessor.services.input.EMail
import com.cuupa.mailprocessor.services.input.Zip
import com.cuupa.mailprocessor.services.input.email.EmailService
import com.cuupa.mailprocessor.services.input.scan.ScanService
import com.cuupa.mailprocessor.userconfiguration.EmailProperties
import com.cuupa.mailprocessor.userconfiguration.ScanProperties
import org.apache.juli.logging.LogFactory
import org.camunda.bpm.engine.RuntimeService
import org.springframework.scheduling.annotation.Scheduled
import java.io.Serializable

class WorkerService(private val runtimeService: RuntimeService,
                    private val mailprocessorConfiguration: MailprocessorConfiguration,
                    private val scanService: ScanService, private val emailService: EmailService) {

    @Scheduled(cron = "*/30 * * * * *")
    fun execute() {
        val userConfigurationList = mailprocessorConfiguration.configurations
        userConfigurationList.forEach { config ->
            if (config.emailProperties.isEnabled) {
                executeEMail(config.username!!, config.emailProperties)
            }
            if (config.scanProperties.isEnabled) {
                executeScanMail(config.username!!, config.scanProperties)
            }
        }
    }

    private fun executeEMail(username: String, config: EmailProperties) {
        val emails = emailService.loadMails(username, config).map { getEmailProperties(it) }
        emails.forEach {
            try {
                runtimeService.startProcessInstanceByKey("mailprocessor", it)
            } catch (exception: Exception) {
                LOG.error(exception)
            }
        }
    }

    private fun executeScanMail(username: String, config: ScanProperties) {
        val scans = scanService.loadScans(username, config).map {
            setScanOrZipProperties(it, getScanProcessProperties(it))
        }
        scans.forEach {
            try {
                runtimeService.startProcessInstanceByKey("mailprocessor", it)
            } catch (exception: Exception) {
                LOG.error(exception)
            }
        }
    }

    private fun setScanOrZipProperties(it: Document,
                                       properties: MutableMap<String, Serializable?>): Map<String, Serializable?> {
        when (it) {
            is Zip -> {
                properties[ProcessProperty.ORIGINAL_FILENAME.name] = it.originalFileName
                properties[ProcessProperty.MAIL_TYPE.name] = "zip"
            }
            else -> properties[ProcessProperty.MAIL_TYPE.name] = "scan"
        }
        return properties
    }

    private fun getScanProcessProperties(it: Document) = mutableMapOf(ProcessProperty.FILE_NAME.name to it.filename,
                                                                      ProcessProperty.FILE_CONTENT.name to it.content,
                                                                      ProcessProperty.USERNAME.name to it.user)

    private fun getEmailProperties(it: EMail) = mapOf(ProcessProperty.FILE_NAME.name to it.filename,
                                                      ProcessProperty.FILE_CONTENT.name to it.content,
                                                      ProcessProperty.USERNAME.name to it.user,
                                                      ProcessProperty.EMAIL_LABEL.name to it.label,
                                                      ProcessProperty.EMAIL_SUBJECT.name to it.subject,
                                                      ProcessProperty.RECEIVED_DATE.name to it.receivedDate,
                                                      ProcessProperty.EMAIL_ATTACHMENTS.name to it.attachments,
                                                      ProcessProperty.MAIL_TYPE.name to "email")

    companion object {
        private val LOG = LogFactory.getLog(WorkerService::class.java)
    }
}