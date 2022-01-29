package com.cuupa.mailprocessor.services

import com.cuupa.mailprocessor.MailprocessorConfiguration
import com.cuupa.mailprocessor.process.ProcessProperty
import com.cuupa.mailprocessor.services.input.Document
import com.cuupa.mailprocessor.services.input.EMail
import com.cuupa.mailprocessor.services.input.InputType
import com.cuupa.mailprocessor.services.input.Zip
import com.cuupa.mailprocessor.services.input.email.EmailService
import com.cuupa.mailprocessor.services.input.scan.ScanService
import com.cuupa.mailprocessor.userconfiguration.DirectoryConfiguration
import com.cuupa.mailprocessor.userconfiguration.EmailConfiguration
import org.apache.juli.logging.LogFactory
import org.camunda.bpm.engine.RuntimeService
import org.springframework.scheduling.annotation.Scheduled
import java.io.Serializable

class WorkerService(
    private val runtimeService: RuntimeService,
    private val mailprocessorConfiguration: MailprocessorConfiguration,
    private val scanService: ScanService, private val emailService: EmailService
) {

    @Scheduled(cron = "* * * * * *")
    fun execute() {
        mailprocessorConfiguration.configurations.entries?.forEach { config ->

            if (config.input?.email?.enabled == true) {
                executeEMail(config.username!!, config.input?.email!!)
            }
            if (config.input?.directory?.enabled == true) {
                executeScanMail(config.username!!, config.input?.directory!!)
            }
        }
    }

    private fun executeEMail(username: String, config: EmailConfiguration) {
        val emails = emailService.loadMails(username, config).map { getEmailProperties(it) }
        emails.forEach {
            try {
                runtimeService.startProcessInstanceByKey("mailprocessor", it)
            } catch (exception: Exception) {
                LOG.error(exception)
            }
        }
    }

    private fun executeScanMail(username: String, config: DirectoryConfiguration) {
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

    private fun setScanOrZipProperties(
        document: Document,
        properties: MutableMap<String, Serializable?>
    ): Map<String, Serializable?> {
        when (document) {
            is Zip -> {
                properties[ProcessProperty.ZIP_FILE_NAME.name] = document.originalFileName
                properties[ProcessProperty.MAIL_TYPE.name] = InputType.ZIP.name
            }
            else -> properties[ProcessProperty.MAIL_TYPE.name] = InputType.SCAN.name
        }
        return properties
    }

    private fun getScanProcessProperties(document: Document) = mutableMapOf(
        ProcessProperty.FILE_NAME.name to document.filename,
        ProcessProperty.FILE_CONTENT.name to document.content,
        ProcessProperty.USERNAME.name to document.user,
        ProcessProperty.ARCHIVED.name to false

    )

    private fun getEmailProperties(email: EMail) = mapOf(
        ProcessProperty.FILE_NAME.name to email.filename,
        ProcessProperty.FILE_CONTENT.name to email.content,
        ProcessProperty.USERNAME.name to email.user,
        ProcessProperty.EMAIL_LABEL.name to email.label,
        ProcessProperty.EMAIL_SUBJECT.name to email.subject,
        ProcessProperty.RECEIVED_DATE.name to email.receivedDate,
        ProcessProperty.EMAIL_ATTACHMENTS.name to email.attachments,
        ProcessProperty.ARCHIVED.name to false,
        ProcessProperty.MAIL_TYPE.name to InputType.EMAIL.name
    )

    companion object {
        private val LOG = LogFactory.getLog(WorkerService::class.java)
    }
}