package com.cuupa.mailprocessor.configuration

import com.cuupa.mailprocessor.delegates.ocr.CheckOCRResultWaitTimeDelegate
import com.cuupa.mailprocessor.delegates.ocr.ExtractTextDelegate
import com.cuupa.mailprocessor.delegates.ocr.OCRIdDelegate
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class OcrDelegateConfiguration {

    @Bean
    open fun extractTextDelegate() = ExtractTextDelegate()

     @Bean
     open fun callOcrDelegate() = OcrDelegateConfiguration()

    @Bean
    open fun checkOcrResultWaitTimeDelegate() = CheckOCRResultWaitTimeDelegate()

    @Bean
    open fun ocrIdDelegate() = OCRIdDelegate()
}