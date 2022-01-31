package com.cuupa.mailprocessor.services

import com.cuupa.mailprocessor.MailprocessorConfiguration
import com.cuupa.mailprocessor.MailprocessorStartService
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
    private val startService: MailprocessorStartService,
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
        emails.forEach { startService.startEmailProcess(it) }
    }

    private fun executeScanMail(username: String, config: DirectoryConfiguration) {
        val scans = scanService.loadScans(username, config).map { getScanProcessProperties(it) }
        scans.forEach { startService.startScanProcess(it) }
    }

    private fun getScanProcessProperties(document: Document) = mutableMapOf(
        ProcessProperty.FILE_CONTENT.name to document.content,

    )

    private fun getEmailProperties(email: EMail) = mapOf(
        ProcessProperty.FILE_CONTENT.name to email.content,
    )

    companion object {
        private val LOG = LogFactory.getLog(WorkerService::class.java)
    }
}