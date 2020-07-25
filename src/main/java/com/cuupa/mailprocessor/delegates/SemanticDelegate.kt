package com.cuupa.mailprocessor.delegates

import com.cuupa.mailprocessor.process.ProcessInstanceHandler
import com.cuupa.mailprocessor.services.semantic.ExternSemanticService
import com.cuupa.mailprocessor.services.semantic.Metadata
import com.cuupa.mailprocessor.services.semantic.SemanticResult
import org.apache.commons.collections4.CollectionUtils
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.engine.delegate.JavaDelegate
import java.util.function.Consumer

class SemanticDelegate(private val externSemanticService: ExternSemanticService) : JavaDelegate {

    override fun execute(delegateExecution: DelegateExecution) {
        val handler = ProcessInstanceHandler(delegateExecution)

        val semanticResult = externSemanticService.getSemanticResult(
                handler.plainText.joinToString(" ", "", ""))

        semanticResult.forEach(Consumer { result: SemanticResult ->
            val metadata = CollectionUtils.emptyIfNull(result.metaData)
            handler.addTopic(result.topicName)
            handler.sender = result.sender
            handler.addMetaData(metadata
                    .filter { e: Metadata -> "sender" != e.name }
            )
        })
    }
}