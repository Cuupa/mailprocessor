package com.cuupa.mailprocessor.delegates

import com.cuupa.mailprocessor.process.ProcessInstanceHandler
import com.cuupa.mailprocessor.services.TranslateService
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.engine.delegate.JavaDelegate

class TranslateTopicDelegate(private val translateService: TranslateService) : JavaDelegate {
    override fun execute(delegateExecution: DelegateExecution) {
        val handler = ProcessInstanceHandler(delegateExecution)
        handler.topics = handler.topics
                .map { e: String -> translateService.translate(e, handler.locale) }
    }
}