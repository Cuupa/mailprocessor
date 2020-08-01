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
            handler.addMetaData(it.metaData.filter(isNotSenderMetadata()))
        }
    }

    private fun getSender(semanticResult: SemanticResult): String? {
        return when (semanticResult.sender) {
            unknown -> semanticResult.metaData.firstOrNull(isSenderMetadata())?.value ?: unknown
            else -> semanticResult.sender
        }
    }

    private fun getText(handler: ProcessInstanceHandler) = handler.plainText.joinToString(" ", "", "")
    private fun isSenderMetadata(): (Metadata) -> Boolean = { "sender" == it.name }
    private fun isNotSenderMetadata(): (Metadata) -> Boolean = { "sender" != it.name }

    companion object {
        private const val unknown = "UNKNOWN"
    }

}