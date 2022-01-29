package com.cuupa.mailprocessor.configuration

import com.cuupa.mailprocessor.MailProcessorCamundaProcessApplication
import com.cuupa.mailprocessor.MailprocessorConfiguration
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.annotation.EnableScheduling

@Configuration
@EnableScheduling
@EnableAsync
@Import(DelegateConfiguration::class, ServiceConfiguration::class, CamundaConfiguration::class)
open class ApplicationConfiguration {

    @Value("\${config.path}")
    private lateinit var configpath: String

    @Bean
    open fun mailProcessorProcessApplication(): MailProcessorCamundaProcessApplication {
        return MailProcessorCamundaProcessApplication()
    }

    @Bean
    open fun configuration(): MailprocessorConfiguration {
        return MailprocessorConfiguration(configpath)
    }
}