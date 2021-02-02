package com.cuupa.mailprocessor.configuration

import com.cuupa.mailprocessor.services.TextExtractorService
import com.cuupa.mailprocessor.services.TranslateService
import com.cuupa.mailprocessor.services.WorkerService
import com.cuupa.mailprocessor.services.camunda.DmnDeployService
import com.cuupa.mailprocessor.services.input.email.EmailService
import com.cuupa.mailprocessor.services.input.scan.ScanService
import com.cuupa.mailprocessor.services.reminder.ReminderService
import com.cuupa.mailprocessor.services.semantic.ExternSemanticService
import org.camunda.bpm.engine.RepositoryService
import org.camunda.bpm.engine.RuntimeService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate

@Configuration
open class ServiceConfiguration {

    @Autowired
    private lateinit var runtimeService: RuntimeService

    @Autowired
    private lateinit var configuration: MailprocessorConfiguration

    @Autowired
    private lateinit var repositoryService: RepositoryService

    @Bean
    open fun externSemanticService(): ExternSemanticService {
        return ExternSemanticService(restTemplate(), configuration.semanticUrl)
    }

    @Bean
    open fun restTemplate(): RestTemplate {
        return RestTemplate()
    }

    @Bean
    open fun workerService(): WorkerService {
        return WorkerService(runtimeService, configuration, scanService(), emailService())
    }

    @Bean
    open fun scanService(): ScanService {
        return ScanService()
    }

    @Bean
    open fun emailService(): EmailService {
        return EmailService()
    }

    @Bean
    open fun translateService(): TranslateService {
        return TranslateService()
    }

    @Bean
    open fun reminderService(): ReminderService {
        return ReminderService()
    }

    @Bean
    open fun textExtractorService(): TextExtractorService {
        return TextExtractorService()
    }

    @Bean
    open fun dmnDeployService(): DmnDeployService {
        return DmnDeployService(repositoryService, configuration.decisiontables)
    }
}