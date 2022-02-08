package com.cuupa.mailprocessor.services

import com.cuupa.mailprocessor.MailprocessorConfiguration
import com.cuupa.mailprocessor.process.CallActivityConstants
import com.cuupa.mailprocessor.process.DmnConstants
import com.cuupa.mailprocessor.process.ProcessProperty
import com.cuupa.mailprocessor.services.files.transfer.TransferProtocolFacade
import com.cuupa.mailprocessor.services.input.Document
import com.cuupa.mailprocessor.services.input.EMail
import com.cuupa.mailprocessor.services.input.email.EmailService
import com.cuupa.mailprocessor.services.input.scan.ScanService
import com.cuupa.mailprocessor.userconfiguration.DirectoryConfiguration
import com.cuupa.mailprocessor.userconfiguration.EmailConfiguration
import com.cuupa.mailprocessor.userconfiguration.WorkDirectory
import org.apache.juli.logging.LogFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.util.Assert

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

    @Scheduled(fixedDelay = `2minutes`)
    fun executeScanMail() {
        mailprocessorConfiguration.configurations.entries?.forEach { userConfiguration ->
            val scanConfig = userConfiguration.input?.directory ?: DirectoryConfiguration()

            if (scanConfig.enabled) {
                val scans =
                    scanService.loadScans(userConfiguration.username!!, scanConfig)

                scans.forEach {
                    writeContent(
                        it.id.toString(),
                        it.content!!,
                        mailprocessorConfiguration.configurations.workdirectory
                    )
                    val payload = getScanProcessProperties(it)
                    startService.startScanProcess(payload)
                }
            }
        }
    }

    private fun getScanProcessProperties(document: Document) = mapOf(
        ProcessProperty.ID.value to document.id.toString(),
        ProcessProperty.FILE_NAME.value to document.filename,
        CallActivityConstants.PREPROCESSING_CALL to CallActivityConstants.SCAN_PREPROCESSING,
        CallActivityConstants.CONVERTING_CALL to CallActivityConstants.CONVERTING,
        CallActivityConstants.OCR_CALL to CallActivityConstants.OCR,
        CallActivityConstants.CLASSIFICATION to CallActivityConstants.CLASSIFICATION,
        CallActivityConstants.QA_CALL to CallActivityConstants.QA,
        CallActivityConstants.ARCHIVING_CALL to CallActivityConstants.ARCHIVING,
        DmnConstants.CHECK_CONVERTING_DMN to "${document.user}_converting"
    )

    private fun writeContent(id: String?, content: ByteArray?, workConfig: WorkDirectory?) {
        Assert.notNull(id, "ID must not be null")
        Assert.notNull(workConfig, "WorkConfiguration must not be null")
        Assert.notNull(content, "content must not be null")

        TransferProtocolFacade.getForPath(workConfig?.path)
            .init(workConfig?.username, workConfig?.password).use { file ->
                file.createDirectory(workConfig?.path!!)
                file.save(workConfig.path!!, id!!, content!!)
            }
    }

    private fun getEmailProperties(email: EMail) = mapOf<String, Any?>(
        //ProcessProperty.CONTENT.name to email.content,
    )

    companion object {
        private val LOG = LogFactory.getLog(WorkerService::class.java)

        const val `30minutes` = 1800000L
        const val `2minutes` = 120000L
    }
}

