package com.cuupa.mailprocessor.services

import com.cuupa.mailprocessor.MailprocessorConfiguration
import com.cuupa.mailprocessor.services.input.ScanService
import com.cuupa.mailprocessor.userconfiguration.EmailProperties
import com.cuupa.mailprocessor.userconfiguration.ScanProperties
import org.camunda.bpm.engine.RuntimeService
import org.springframework.scheduling.annotation.Scheduled

class WorkerService(private val runtimeService: RuntimeService,
                    private val mailprocessorConfiguration: MailprocessorConfiguration,
                    private val scanService: ScanService) {

    @Scheduled(cron = "*/30 * * * *")
    fun execute() {
        val userConfigurationList = mailprocessorConfiguration.configurations
        userConfigurationList.forEach { config ->
            if (config.emailProperties.isEnabled) {
                executeEMail(config.emailProperties)
            }
            if (config.scanProperties.isEnabled) {
                executeScanMail(config.scanProperties)
            }
        }
    }

    private fun executeEMail(config: EmailProperties) {}
    private fun executeScanMail(config: ScanProperties) {}

}