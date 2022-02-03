package com.cuupa.mailprocessor.configuration

import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@Import(value = [PreprocessingDelegateConfiguration::class,
    OcrDelegateConfiguration::class,
    ClassificationDelegateConfiguration::class,
    ConversionConfiguration::class])
open class DelegateConfiguration {


}