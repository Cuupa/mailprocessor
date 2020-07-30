package com.cuupa.mailprocessor.configuration

import com.cuupa.mailprocessor.MailprocessorConfiguration
import com.cuupa.mailprocessor.services.TranslateService
import com.cuupa.mailprocessor.services.WorkerService
import com.cuupa.mailprocessor.services.input.ScanService
import com.cuupa.mailprocessor.services.reminder.ReminderService
import com.cuupa.mailprocessor.services.semantic.ExternSemanticService
import org.camunda.bpm.engine.RuntimeService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate

@Configuration
open class ServiceConfiguration {

    @Value("\${mailprocessor.semanticUrl}")
    private val semanticUrl: String? = null

    @Autowired
    private lateinit var runtimeService: RuntimeService

    @Autowired
    private lateinit var mailprocessorConfiguration: MailprocessorConfiguration

    @Bean
    open fun externSemanticService(): ExternSemanticService {
        return ExternSemanticService(restTemplate(), semanticUrl!!)
    }

    @Bean
    open fun restTemplate(): RestTemplate {
        return RestTemplate()
    }

    @Bean
    open fun workerService(): WorkerService {
        return WorkerService(runtimeService, mailprocessorConfiguration, scanService())
    }

    @Bean
    open fun scanService(): ScanService {
        return ScanService()
    }

    @Bean
    open fun translateService(): TranslateService {
        return TranslateService()
    }

    @Bean
    open fun reminderService(): ReminderService {
        return ReminderService()
    }
}