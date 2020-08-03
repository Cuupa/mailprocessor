package com.cuupa.mailprocessor.delegates

import com.cuupa.mailprocessor.MailprocessorConfiguration
import com.cuupa.mailprocessor.process.ProcessInstanceHandler
import com.cuupa.mailprocessor.services.input.scan.ScanService
import com.cuupa.mailprocessor.userconfiguration.ScanProperties
import org.apache.juli.logging.LogFactory
import org.camunda.bpm.engine.RuntimeService
import org.camunda.bpm.engine.delegate.DelegateExecution

class ArchivingErrorDelegate(scanService: ScanService, runtimeService: RuntimeService,
                             private val configuration: MailprocessorConfiguration) : ArchivingEventDelegate(scanService,
                                                                                                             runtimeService) {

    override fun execute(delegateExecution: DelegateExecution) {
        val handler = ProcessInstanceHandler(delegateExecution)
        val scanProperties = configuration.getConfigurationForUser(handler.username).scanProperties

        when {
            handler.archived -> return
            handler.isScanMail -> processScan(scanProperties.errorFolder!!, scanProperties, handler)
            handler.isZipFile -> processZip(scanProperties.errorFolder!!, scanProperties, handler)
        }
        log.warn(handler.errors.joinToString("\n", "", ""))
        log.warn("Error archiving ${handler.fileName}")
    }

    companion object {
        private val log = LogFactory.getLog(ArchivingErrorDelegate::class.java)
    }
}
