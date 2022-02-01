package com.cuupa.mailprocessor.delegates.classification

import com.cuupa.mailprocessor.api.classification.client.ClassificationClient
import com.cuupa.mailprocessor.api.classification.model.ClassificationRequest
import com.cuupa.mailprocessor.api.classification.model.Metadata
import com.cuupa.mailprocessor.process.ProcessVariables
import com.cuupa.mailprocessor.services.Extensions.orFalse
import com.cuupa.mailprocessor.userconfiguration.ClassificatorConfiguration
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.engine.delegate.JavaDelegate
import java.util.*

class CallClassificationDelegate(
    private val config: ClassificatorConfiguration?,
    private val client: ClassificationClient
) : JavaDelegate {

    override fun execute(execution: DelegateExecution?) {
        if (config?.enabled.orFalse()) {
            return
        }

        val variables = ProcessVariables(execution)

        val request = ClassificationRequest().apply {
            content = Base64.getEncoder().encodeToString(variables.content)
            contentType = variables.contentType.toString()
            apiKey = config?.apiKey
        }

        val results = client.classify(request)

        results?.let { result ->
            variables.topics = result.results.map { it.topic }
            variables.senders = result.results.map { it.sender }
            variables.metadatas = map(result.results.flatMap { it.metadata })
        }

        variables.hasSemanticResult = variables.topics.isNotEmpty()
            .or(variables.senders.isNotEmpty())
            .or(variables.metadatas.isNotEmpty())
    }

    fun map(values: List<Metadata>): Map<String, List<String>> {
        val map = mutableMapOf<String, MutableList<String>>()
        values.forEach {
            if (!map.contains(it.name)) {
                map[it.name] = mutableListOf()
            }

            map[it.name]?.add(it.name)
        }

        return map
    }
}