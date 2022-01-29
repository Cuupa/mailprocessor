package com.cuupa.mailprocessor.configuration

import com.cuupa.mailprocessor.MailprocessorConfiguration
import com.cuupa.mailprocessor.delegates.*
import com.cuupa.mailprocessor.services.TextExtractorService
import com.cuupa.mailprocessor.services.TranslateService
import com.cuupa.mailprocessor.services.input.email.EmailService
import com.cuupa.mailprocessor.services.input.scan.ScanService
import com.cuupa.mailprocessor.services.semantic.ExternSemanticService
import org.camunda.bpm.engine.RuntimeService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
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

    @Autowired
    private val runtimeService: RuntimeService? = null


}