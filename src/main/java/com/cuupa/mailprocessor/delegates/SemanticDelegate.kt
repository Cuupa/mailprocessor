package com.cuupa.mailprocessor.delegates

import com.cuupa.mailprocessor.process.ProcessInstanceHandler
import com.cuupa.mailprocessor.services.semantic.ExternSemanticService
import com.cuupa.mailprocessor.services.semantic.Metadata
import com.cuupa.mailprocessor.services.semantic.SemanticResult
import org.apache.commons.collections4.CollectionUtils
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.engine.delegate.JavaDelegate
import org.springframework.web.client.ResourceAccessException

class SemanticDelegate(private val externSemanticService: ExternSemanticService) : JavaDelegate {

    override fun execute(delegateExecution: DelegateExecution) {
        val handler = ProcessInstanceHandler(delegateExecution)
        getSemanticResult(handler).forEach { result: SemanticResult ->
            val metadata = CollectionUtils.emptyIfNull(result.metaData)
            handler.addTopic(result.topicName)
            handler.sender = result.sender
            handler.addMetaData(metadata.filter { e: Metadata -> "sender" != e.name })
        }
    }

    private fun getSemanticResult(handler: ProcessInstanceHandler): List<SemanticResult> {
        return try {
            externSemanticService.getSemanticResult(handler.plainText.joinToString(" ", "", ""))
        } catch (exception: ResourceAccessException) {
            listOf(SemanticResult("OTHER"))
        }
    }
}