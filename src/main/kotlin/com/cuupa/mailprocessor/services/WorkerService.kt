package com.cuupa.mailprocessor.services

import com.cuupa.mailprocessor.MailprocessorConfiguration
import com.cuupa.mailprocessor.MailprocessorStartService
import com.cuupa.mailprocessor.process.CallActivityConstants
import com.cuupa.mailprocessor.process.ProcessProperty
import com.cuupa.mailprocessor.services.input.Document
import com.cuupa.mailprocessor.services.input.EMail
import com.cuupa.mailprocessor.services.input.email.EmailService
import com.cuupa.mailprocessor.services.input.scan.ScanService
import com.cuupa.mailprocessor.userconfiguration.DirectoryConfiguration
import com.cuupa.mailprocessor.userconfiguration.EmailConfiguration
import org.apache.juli.logging.LogFactory
import org.springframework.scheduling.annotation.Scheduled

class WorkerService(
    private val startService: MailprocessorStartService,
    private val mailprocessorConfiguration: MailprocessorConfiguration,
    private val scanService: ScanService, private val emailService: EmailService
) {

    @Scheduled(fixedDelay = `30minutes`)
    fun executeEMail() {
        mailprocessorConfiguration.configurations.entries?.forEach { userConfiguration ->
            val emailConfig = userConfiguration.input?.email ?: EmailConfiguration()

            if (emailConfig.enabled) {
                val emails = emailService.loadMails(userConfiguration.username!!, emailConfig)
                    .map { getEmailProperties(it) }
                emails.forEach { startService.startEmailProcess(it) }
            }
        }
    }

    @Scheduled(fixedDelay = `30minutes`)
    fun executeScanMail() {
        mailprocessorConfiguration.configurations.entries?.forEach { userConfiguration ->
            val scanConfig = userConfiguration.input?.directory ?: DirectoryConfiguration()

            if (scanConfig.enabled) {
                val scans =
                    scanService.loadScans(userConfiguration.username!!, scanConfig).map { getScanProcessProperties(it) }
                scans.forEach { startService.startScanProcess(it) }
            }
        }
    }

    private fun getScanProcessProperties(document: Document) = mapOf(
        ProcessProperty.FILE_CONTENT.name to document.content,
        ProcessProperty.FILE_NAME.name to document.filename,
        CallActivityConstants.PREPROCESSING_CALL to CallActivityConstants.SCAN_PREPROCESSING,
        CallActivityConstants.CONVERTING_CALL to CallActivityConstants.CONVERTING,
        CallActivityConstants.OCR_CALL to CallActivityConstants.OCR,
        CallActivityConstants.CLASSIFICATION to CallActivityConstants.CLASSIFICATION,
        CallActivityConstants.QA_CALL to CallActivityConstants.QA,
        CallActivityConstants.ARCHIVING_CALL to CallActivityConstants.ARCHIVING
    )

    private fun getEmailProperties(email: EMail) = mapOf(
        ProcessProperty.FILE_CONTENT.name to email.content,
    )

    companion object {
        private val LOG = LogFactory.getLog(WorkerService::class.java)

        const val `30minutes` = 1800000L
    }
}