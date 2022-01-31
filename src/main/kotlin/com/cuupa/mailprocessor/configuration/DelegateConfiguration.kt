package com.cuupa.mailprocessor.configuration

import com.cuupa.mailprocessor.delegates.preprocessing.scan.DetectDpiDelegate
import com.cuupa.mailprocessor.delegates.preprocessing.scan.DetectPatchSheetDelegate
import com.cuupa.mailprocessor.delegates.preprocessing.scan.DetermineFiletypeDelegate
import com.cuupa.mailprocessor.services.BarcodeReader
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class DelegateConfiguration {

    @Bean("detectPatchSheetDelegate")
    open fun detectPatchSheetDelegate(barcodeReader: BarcodeReader) = DetectPatchSheetDelegate(barcodeReader)

    @Bean("detectDPIDelegate")
    open fun detectDPIDelegate() = DetectDpiDelegate()

    @Bean("determineFiletypeDelegate")
    open fun determineFiletypeDelegate() = DetermineFiletypeDelegate()
}