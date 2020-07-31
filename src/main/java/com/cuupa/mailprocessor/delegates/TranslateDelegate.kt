package com.cuupa.mailprocessor.delegates

import com.cuupa.mailprocessor.MailprocessorConfiguration
import com.cuupa.mailprocessor.process.ProcessInstanceHandler
import com.cuupa.mailprocessor.services.TranslateService
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.engine.delegate.JavaDelegate
import java.util.*

class TranslateDelegate(private val configuration: MailprocessorConfiguration,
                        private val translateService: TranslateService) : JavaDelegate {

    override fun execute(delegateExecution: DelegateExecution) {
        val handler = ProcessInstanceHandler(delegateExecution)
        val locale = configuration.getConfigurationForUser(handler.username).locale
        handler.topics = translateTopics(handler, locale)
        if (handler.sender != null) {
            handler.sender = translateService.translate(handler.sender!!, locale)
        }
    }

    private fun translateTopics(handler: ProcessInstanceHandler,
                                locale: Locale) = handler.topics.map { translateService.translate(it, locale) }
}