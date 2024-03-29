package com.cuupa.mailprocessor.configuration

import com.cuupa.mailprocessor.MailprocessorConfiguration
import com.cuupa.mailprocessor.delegates.ocr.CheckOCRResultWaitTimeDelegate
import com.cuupa.mailprocessor.delegates.ocr.ExtractTextDelegate
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class OcrDelegateConfiguration {

    @Bean
    open fun extractTextDelegate(configuration: MailprocessorConfiguration) =
        ExtractTextDelegate(configuration.configurations.workdirectory)

    @Bean
    open fun callOcrDelegate() = OcrDelegateConfiguration()

    @Bean
    open fun checkOcrResultWaitTimeDelegate() = CheckOCRResultWaitTimeDelegate()
}