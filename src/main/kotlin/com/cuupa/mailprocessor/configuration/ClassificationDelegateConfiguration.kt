package com.cuupa.mailprocessor.configuration

import com.cuupa.mailprocessor.MailprocessorConfiguration
import com.cuupa.mailprocessor.api.classification.client.ClassificationClient
import com.cuupa.mailprocessor.delegates.classification.CallClassificationDelegate
import com.cuupa.mailprocessor.userconfiguration.Configuration
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean

@org.springframework.context.annotation.Configuration
open class ClassificationDelegateConfiguration {

    @Bean("classificationDelegate")
    open fun classifcationDelegate(configuration: MailprocessorConfiguration, client: ClassificationClient) =
        CallClassificationDelegate(configuration.configurations.classificator, client, configuration.configurations.workdirectory)

    @Bean
    open fun client(configuration: MailprocessorConfiguration) = ClassificationClient(configuration.configurations.classificator?.url)
}
