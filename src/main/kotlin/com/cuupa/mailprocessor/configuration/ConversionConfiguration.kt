package com.cuupa.mailprocessor.configuration

import com.cuupa.mailprocessor.delegates.converting.CheckNumberOfConversionRetries
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class ConversionConfiguration {

    @Bean("checkNumberOfConversionTries")
    open fun checkNumberOfConversionRetries() = CheckNumberOfConversionRetries()
}
