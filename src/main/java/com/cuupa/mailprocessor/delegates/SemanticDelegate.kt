package com.cuupa.mailprocessor.delegates

import com.cuupa.mailprocessor.process.ProcessInstanceHandler
import com.cuupa.mailprocessor.services.semantic.ExternSemanticService
import com.cuupa.mailprocessor.services.semantic.Metadata
import com.cuupa.mailprocessor.services.semantic.SemanticResult
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.engine.delegate.JavaDelegate

class SemanticDelegate(private val externSemanticService: ExternSemanticService) : JavaDelegate {

    override fun execute(delegateExecution: DelegateExecution) {
        val handler = ProcessInstanceHandler(delegateExecution)
        externSemanticService.getSemanticResult(getText(handler)).forEach {
            handler.addTopic(it.topicName)
            handler.sender = getSender(it)
            handler.addMetaData(it.metaData.filter(isNotSender()))
        }
    }


    private fun getSender(semanticResult: SemanticResult): String? {
        return if (semanticResult.sender == "UNKNOWN") {
            semanticResult.metaData.firstOrNull(isSender())?.value ?: "UNKNOWN"
        } else {
            semanticResult.sender
        }
    }

    private fun getText(handler: ProcessInstanceHandler) = handler.plainText.joinToString(" ", "", "")
    private fun isSender(): (Metadata) -> Boolean = { "sender" == it.name }
    private fun isNotSender(): (Metadata) -> Boolean = { "sender" != it.name }
}