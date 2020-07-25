package com.cuupa.mailprocessor.services

import com.cuupa.mailprocessor.MailprocessorConfiguration
import com.cuupa.mailprocessor.process.ProcessProperty
import com.cuupa.mailprocessor.services.input.ScanService
import com.cuupa.mailprocessor.userconfiguration.EmailProperties
import com.cuupa.mailprocessor.userconfiguration.ScanProperties
import org.apache.juli.logging.LogFactory
import org.camunda.bpm.engine.RuntimeService
import org.springframework.scheduling.annotation.Scheduled

class WorkerService(private val runtimeService: RuntimeService,
                    private val mailprocessorConfiguration: MailprocessorConfiguration,
                    private val scanService: ScanService) {

    @Scheduled(cron = "*/30 * * * * *")
    fun execute() {
        val userConfigurationList = mailprocessorConfiguration.configurations
        userConfigurationList.forEach { config ->
            if (config.emailProperties.isEnabled) {
                executeEMail(config.emailProperties)
            }
            if (config.scanProperties.isEnabled) {
                executeScanMail(config.username!!, config.scanProperties)
            }
        }
    }

    private fun executeEMail(config: EmailProperties) {}

    private fun executeScanMail(username: String, config: ScanProperties) {
        val map = scanService.loadScans(username, config).map {
            mapOf(ProcessProperty.FILE_NAME.name to it.filename, ProcessProperty.FILE_CONTENT.name
                    to it.content, ProcessProperty.USERNAME.name to it.user)
        }
        map.forEach {
            try {
                runtimeService.startProcessInstanceByKey("mailprocessor", it)
            } catch (exception: Exception) {
                LOG.error(exception)
            }
        }
    }

    companion object {
        private val LOG = LogFactory.getLog(WorkerService::class.java)
    }
}