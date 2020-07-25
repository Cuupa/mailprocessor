package com.cuupa.mailprocessor.delegates

import com.cuupa.mailprocessor.MailprocessorConfiguration
import com.cuupa.mailprocessor.process.ProcessInstanceHandler
import com.cuupa.mailprocessor.services.TranslateService
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.engine.delegate.JavaDelegate

class TranslateDelegate(private val configuration: MailprocessorConfiguration,
                        private val translateService: TranslateService) : JavaDelegate {

    override fun execute(delegateExecution: DelegateExecution) {
        val handler = ProcessInstanceHandler(delegateExecution)
        handler.topics = handler.topics
                .map { e: String ->
                    translateService.translate(e, configuration.getConfigurationForUser(handler
                            .username).locale)
                }
        if (!handler.sender.isNullOrEmpty()) {
            handler.sender = translateService.translate(handler.sender!!, configuration.getConfigurationForUser(handler
                    .username).locale)
        }
    }
}