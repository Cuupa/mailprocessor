package com.cuupa.mailprocessor.configuration

import com.cuupa.mailprocessor.delegates.ocr.ExtractTextDelegate
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class OcrDelegateConfiguration {

    @Bean
    open fun extractTextDelegate() = ExtractTextDelegate()
}