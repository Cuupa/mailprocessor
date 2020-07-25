package com.cuupa.mailprocessor.process

import com.cuupa.mailprocessor.services.semantic.Metadata
import org.apache.commons.lang3.builder.ToStringBuilder
import org.apache.commons.lang3.builder.ToStringStyle
import org.camunda.bpm.engine.delegate.DelegateExecution
import java.util.*

class ProcessInstanceHandler(
        delegateExecution: DelegateExecution?) : AbstractProcessInstanceHandler(delegateExecution!!) {

    fun setPlaintext(textPerPage: List<String>) {
        set(ProcessProperty.PLAIN_TEXT.name, textPerPage)
    }

    val plainText: List<String>
        get() = getAsListOfString(ProcessProperty.PLAIN_TEXT.name)

    val fileContent: ByteArray
        get() = getAsByteArray(ProcessProperty.FILE_CONTENT.name)

    fun addTopic(topic: String?): ProcessInstanceHandler {
        add(ProcessProperty.TOPIC.name, topic)
        return this
    }

    fun setTopics(topics: List<String>): ProcessInstanceHandler {
        add(ProcessProperty.TOPIC.name, topics)
        return this
    }

    var topics: List<String> = mutableListOf()
        get() = getAsListOfString(ProcessProperty.TOPIC.name)

    fun setSender(sender: String?): ProcessInstanceHandler {
        set(ProcessProperty.SENDER.name, sender)
        return this
    }

    val sender: String?
        get() = getAsString(ProcessProperty.SENDER.name)

    fun addMetaData(
            metadata: List<Metadata>): ProcessInstanceHandler {
        add(ProcessProperty.METADATA.name, metadata)
        return this
    }

    val metadata: List<Metadata>
        get() = getAsListOf(ProcessProperty.METADATA.name)

    val dmnResult: Map<String, Any>
        get() = getAsMap(ProcessProperty.DMN_RESULT.name)

    fun setPathToSave(pathToSave: String?): ProcessInstanceHandler {
        add(ProcessProperty.PATH_TO_SAVE.name, pathToSave)
        return this
    }

    val pathToSave: String?
        get() = getAsString(ProcessProperty.PATH_TO_SAVE.name)

    fun setHasReminder(reminder: Boolean): ProcessInstanceHandler {
        add(ProcessProperty.HAS_REMINDER.name, reminder)
        return this
    }

    val locale: Locale
        get() = Locale.getDefault()

    val fileName: String?
        get() = getAsString(ProcessProperty.FILE_NAME.name)

    override fun toString(): String {
        return ToStringBuilder.reflectionToString(delegateExecution.variables,
                ToStringStyle.MULTI_LINE_STYLE)
    }
}