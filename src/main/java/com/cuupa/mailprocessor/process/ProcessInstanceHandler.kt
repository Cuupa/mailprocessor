package com.cuupa.mailprocessor.process

import com.cuupa.mailprocessor.services.semantic.Metadata
import org.apache.commons.lang3.builder.ToStringBuilder
import org.apache.commons.lang3.builder.ToStringStyle
import org.camunda.bpm.engine.delegate.DelegateExecution

class ProcessInstanceHandler(
        delegateExecution: DelegateExecution?) : AbstractProcessInstanceHandler(delegateExecution!!) {

    var reminderDate: String?
        get() = getAsString(ProcessProperty.REMINDER_DATE.name)
        set(value) = set(ProcessProperty.REMINDER_DATE.name, value)
    var plainText: List<String>
        get() = getAsListOfString(ProcessProperty.PLAIN_TEXT.name)
        set(value) = set(ProcessProperty.PLAIN_TEXT.name, value)

    var fileContent: ByteArray
        get() = getAsByteArray(ProcessProperty.FILE_CONTENT.name) ?: ByteArray(0)
        set(value) = set(ProcessProperty.FILE_CONTENT.name, value)

    var username: String
        get() = getAsString(ProcessProperty.USERNAME.name) ?: ""
        set(value) = set(ProcessProperty.USERNAME.name, value)

    var topics: List<String>
        get() = getAsListOfString(ProcessProperty.TOPICS.name)
        set(value) = set(ProcessProperty.TOPICS.name, value)

    var sender: String?
        get() = getAsString(ProcessProperty.SENDER.name)
        set(value) = set(ProcessProperty.SENDER.name, value)

    var metadata: List<Metadata>
        get() = getAsListOf(ProcessProperty.METADATA.name)
        set(value) = set(ProcessProperty.METADATA.name, value)

    val dmnResult: Map<String, Any>
        get() = getAsMap(ProcessProperty.DMN_RESULT.name)

    var pathToSave: String?
        get() = getAsString(ProcessProperty.PATH_TO_SAVE.name)
        set(value) = set(ProcessProperty.PATH_TO_SAVE.name, value)

    var hasReminder: Boolean
        get() = getAsBooleanDefaultFalse(ProcessProperty.HAS_REMINDER.name)
        set(value) = set(ProcessProperty.HAS_REMINDER.name, value)

    val fileName: String?
        get() = getAsString(ProcessProperty.FILE_NAME.name)

    fun addTopic(topic: String?): ProcessInstanceHandler {
        add(ProcessProperty.TOPICS.name, topic)
        return this
    }

    fun setHasReminder(reminder: Boolean): ProcessInstanceHandler {
        add(ProcessProperty.HAS_REMINDER.name, reminder)
        return this
    }

    fun addMetaData(metadata: List<Metadata>): ProcessInstanceHandler {
        add(ProcessProperty.METADATA.name, metadata)
        return this
    }

    override fun toString(): String {
        return ToStringBuilder.reflectionToString(delegateExecution.variables,
                ToStringStyle.MULTI_LINE_STYLE)
    }
}