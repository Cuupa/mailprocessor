package com.cuupa.mailprocessor.configuration

import com.cuupa.mailprocessor.MailprocessorConfiguration
import com.cuupa.mailprocessor.delegates.*
import com.cuupa.mailprocessor.services.TextExtractorService
import com.cuupa.mailprocessor.services.TranslateService
import com.cuupa.mailprocessor.services.input.email.EmailService
import com.cuupa.mailprocessor.services.input.scan.ScanService
import com.cuupa.mailprocessor.services.semantic.ExternSemanticService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@Import(ReminderDelegateConfiguration::class)
open class DelegateConfiguration {

    @Autowired
    private val externSemanticService: ExternSemanticService? = null

    @Autowired
    private val mailprocessorConfiguration: MailprocessorConfiguration? = null

    @Autowired
    private val translateService: TranslateService? = null

    @Autowired
    private val scanService: ScanService? = null

    @Autowired
    private val emailService: EmailService? = null

    @Autowired
    private val textExtractorService: TextExtractorService? = null

    @Bean
    open fun plaintextDelegate() = PlaintextDelegate(textExtractorService!!)

    @Bean
    open fun semanticDelegate() = SemanticDelegate(externSemanticService!!)

    @Bean
    open fun dmnResultMapperDelegate() = DmnResultMapperDelegate()

    @Bean
    open fun translateTopicDelegate() = TranslateDelegate(mailprocessorConfiguration!!, translateService!!)

    @Bean
    open fun archiveDelegate() = ArchiveDelegate(mailprocessorConfiguration!!, translateService!!)

    @Bean
    open fun defaultValueDelegate() = DefaultValueDelegate()

    @Bean
    open fun handleArchivingErrorDelegate() = ArchivingErrorDelegate(scanService!!, mailprocessorConfiguration!!)

    @Bean
    open fun handleArchivingSuccessDelegate() = ArchivingSuccessDelegate(scanService!!,
                                                                         emailService!!,
                                                                         mailprocessorConfiguration!!)

    @Bean
    open fun embedTopicsDelegate() = EmbedTopicsDelegate()
}