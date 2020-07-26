package com.cuupa.mailprocessor.delegates

import com.cuupa.mailprocessor.MailprocessorConfiguration
import com.cuupa.mailprocessor.process.ProcessInstanceHandler
import com.cuupa.mailprocessor.services.input.ScanService
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.engine.delegate.JavaDelegate

class ArchivingErrorDelegate(private val scanService: ScanService,
                             private val configuration: MailprocessorConfiguration) : JavaDelegate {

    override fun execute(delegateExecution: DelegateExecution) {
        val handler = ProcessInstanceHandler(delegateExecution)
        if (handler.archived) {
            return
        }

        val scanProperties = configuration.getConfigurationForUser(handler.username).scanProperties
        scanService.moveScan(handler.fileName, handler.fileContent, scanProperties.errorPath, scanProperties)
    }

}
