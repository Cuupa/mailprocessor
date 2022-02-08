package com.cuupa.mailprocessor.configuration

import com.cuupa.mailprocessor.MailprocessorConfiguration
import com.cuupa.mailprocessor.delegates.preprocessing.scan.*
import com.cuupa.mailprocessor.services.BarcodeReader
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class PreprocessingDelegateConfiguration {

    @Autowired
    private lateinit var mailprocessorConfiguration: MailprocessorConfiguration

    @Bean("detectPatchSheetDelegate")
    open fun detectPatchSheetDelegate(barcodeReader: BarcodeReader) = DetectPatchSheetDelegate(barcodeReader, mailprocessorConfiguration.configurations.workdirectory!!)

    @Bean("detectDPIDelegate")
    open fun detectDPIDelegate() = DetectDpiDelegate(mailprocessorConfiguration.configurations.workdirectory!!)

    @Bean("determineFiletypeDelegate")
    open fun determineFiletypeDelegate() = DetermineFiletypeDelegate(mailprocessorConfiguration.configurations.workdirectory!!)

    @Bean("colorToggleDelegate")
    open fun colorToggleDelegate() = ColorToggleDelegate(mailprocessorConfiguration.configurations.workdirectory!!)

    @Bean("setDocumentsDelegate")
    open fun setDocumentsDelegate() = SetDocumentsDelegate()
}