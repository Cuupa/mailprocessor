package com.cuupa.mailprocessor.configuration

import com.cuupa.mailprocessor.services.semantic.ExternSemanticService
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate

@Configuration
open class ServiceConfiguration {

    @Value("\${mailprocessor.semanticUrl}")
    private val semanticUrl: String? = null

    @Bean
    open fun externSemanticService(): ExternSemanticService {
        return ExternSemanticService(restTemplate(), semanticUrl!!)
    }

    @Bean
    open fun restTemplate(): RestTemplate {
        return RestTemplate()
    }
}