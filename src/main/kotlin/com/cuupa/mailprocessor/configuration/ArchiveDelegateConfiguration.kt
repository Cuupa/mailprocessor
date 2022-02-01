package com.cuupa.mailprocessor.configuration

import com.cuupa.mailprocessor.MailprocessorConfiguration
import com.cuupa.mailprocessor.delegates.archive.CheckArchiveTargetDelegate
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class ArchiveDelegateConfiguration {

    @Bean("checkArchiveTargetDelegate")
    open fun checkArchiveTargetDelegate(config: MailprocessorConfiguration) =
        CheckArchiveTargetDelegate(config.configurations.entries)
}