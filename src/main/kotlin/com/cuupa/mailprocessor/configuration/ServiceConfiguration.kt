package com.cuupa.mailprocessor.configuration

import com.cuupa.mailprocessor.MailprocessorConfiguration
import com.cuupa.mailprocessor.services.MailprocessorStartService
import com.cuupa.mailprocessor.services.BarcodeReader
import com.cuupa.mailprocessor.services.TextExtractorService
import com.cuupa.mailprocessor.services.TranslateService
import com.cuupa.mailprocessor.services.WorkerService
import com.cuupa.mailprocessor.services.dmn.DmnDeployService
import com.cuupa.mailprocessor.services.input.email.EmailService
import com.cuupa.mailprocessor.services.input.scan.ScanService
import org.camunda.bpm.engine.RepositoryService
import org.camunda.bpm.engine.RuntimeService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class ServiceConfiguration {

    @Autowired
    private lateinit var mailprocessorConfiguration: MailprocessorConfiguration

    @Bean
    open fun workerService(
        startService: MailprocessorStartService,
        scanService: ScanService,
        emailService: EmailService
    ): WorkerService {
        return WorkerService(startService, mailprocessorConfiguration, scanService, emailService)
    }

    @Bean
    open fun startService(runtimeService: RuntimeService): MailprocessorStartService {
        return MailprocessorStartService(runtimeService)
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
    open fun textExtractorService(): TextExtractorService {
        return TextExtractorService()
    }

    @Bean
    open fun barcodeReader() = BarcodeReader()

    @Bean
    open fun dmnDeployService(
        repositoryService: RepositoryService,
        config: MailprocessorConfiguration
    ): DmnDeployService {
        return DmnDeployService(repositoryService, config.configurations.decisiontables?.path)
    }
}