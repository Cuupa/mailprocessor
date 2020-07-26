package com.cuupa.mailprocessor.configuration

import com.cuupa.mailprocessor.MailprocessorConfiguration
import com.cuupa.mailprocessor.delegates.*
import com.cuupa.mailprocessor.services.TranslateService
import com.cuupa.mailprocessor.services.semantic.ExternSemanticService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class DelegateConfiguration {

    @Autowired
    private val externSemanticService: ExternSemanticService? = null

    @Autowired
    private val mailprocessorConfiguration: MailprocessorConfiguration? = null

    @Autowired
    private val translateService: TranslateService? = null

    @Bean
    open fun plaintextDelegate(): PlaintextDelegate {
        return PlaintextDelegate()
    }

    @Bean
    open fun semanticDelegate(): SemanticDelegate {
        return SemanticDelegate(externSemanticService!!)
    }

    @Bean
    open fun dmnResultMapperDelegate(): DmnResultMapperDelegate {
        return DmnResultMapperDelegate()
    }

    @Bean
    open fun translateTopicDelegate(): TranslateDelegate {
        return TranslateDelegate(mailprocessorConfiguration!!, translateService!!)
    }

    @Bean
    open fun archiveDelegate(): ArchiveDelegate {
        return ArchiveDelegate(mailprocessorConfiguration!!, translateService!!)
    }

    @Bean
    open fun defaultValueDelegate(): DefaultValueDelegate {
        return DefaultValueDelegate()
    }
}