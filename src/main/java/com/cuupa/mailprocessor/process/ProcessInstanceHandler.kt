package com.cuupa.mailprocessor.process

import com.cuupa.mailprocessor.services.input.Attachment
import com.cuupa.mailprocessor.services.semantic.Metadata
import org.apache.commons.lang3.builder.ToStringBuilder
import org.apache.commons.lang3.builder.ToStringStyle
import org.camunda.bpm.engine.delegate.DelegateExecution
import java.time.LocalDateTime

class ProcessInstanceHandler(delegateExecution: DelegateExecution?) : AbstractProcessInstanceHandler(delegateExecution!!) {

    val processInstanceId: String
        get() = delegateExecution.processInstanceId

    val receivedDate: LocalDateTime?
        get() = getAsLocalDateTime(ProcessProperty.RECEIVED_DATE)

    val emailSubject: String
        get() = getAsString(ProcessProperty.EMAIL_SUBJECT) ?: ""

    val emailLabel: String
        get() = getAsString(ProcessProperty.EMAIL_LABEL) ?: ""

    val isScanMail: Boolean
        get() = getAsString(ProcessProperty.MAIL_TYPE) == "scan"

    var archived: Boolean
        get() = getAsBooleanDefaultFalse(ProcessProperty.ARCHIVED)
        set(value) = set(ProcessProperty.ARCHIVED, value)

    var reminderDate: String?
        get() = getAsString(ProcessProperty.REMINDER_DATE)
        set(value) = set(ProcessProperty.REMINDER_DATE, value)

    var plainText: List<String>
        get() = getAsListOfString(ProcessProperty.PLAIN_TEXT)
        set(value) = set(ProcessProperty.PLAIN_TEXT, value)

    var fileContent: ByteArray
        get() = getAsByteArray(ProcessProperty.FILE_CONTENT) ?: ByteArray(0)
        set(value) = set(ProcessProperty.FILE_CONTENT, value)

    val attachments: List<Attachment>
        get() = getAsListOf(ProcessProperty.EMAIL_ATTACHMENTS)

    var username: String
        get() = getAsString(ProcessProperty.USERNAME) ?: ""
        set(value) = set(ProcessProperty.USERNAME, value)

    var topics: List<String>
        get() = getAsListOfString(ProcessProperty.TOPICS)
        set(value) = set(ProcessProperty.TOPICS, value)

    var sender: String?
        get() = getAsString(ProcessProperty.SENDER)
        set(value) = set(ProcessProperty.SENDER, value)

    var metadata: List<Metadata>
        get() = getAsListOf(ProcessProperty.METADATA)
        set(value) = set(ProcessProperty.METADATA, value)

    val dmnResult: Map<String, Any>
        get() = getAsMap(ProcessProperty.DMN_RESULT)

    var pathToSave: String?
        get() = getAsString(ProcessProperty.PATH_TO_SAVE)
        set(value) = set(ProcessProperty.PATH_TO_SAVE, value)

    var hasReminder: Boolean
        get() = getAsBooleanDefaultFalse(ProcessProperty.HAS_REMINDER)
        set(value) = set(ProcessProperty.HAS_REMINDER, value)

    var fileName: String?
        get() = getAsString(ProcessProperty.FILE_NAME)
        set(value) = set(ProcessProperty.FILE_NAME, value)

    var archivedFilename: String?
        get() = getAsString(ProcessProperty.ARCHIVED_FILE_NAME)
        set(value) = set(ProcessProperty.ARCHIVED_FILE_NAME, value)

    fun addTopic(topic: String?): ProcessInstanceHandler {
        addToList(ProcessProperty.TOPICS, topic)
        return this
    }

    fun addMetaData(metadata: List<Metadata>): ProcessInstanceHandler {
        addToList(ProcessProperty.METADATA, metadata)
        return this
    }

    override fun toString(): String {
        return ToStringBuilder.reflectionToString(delegateExecution.variables, ToStringStyle.MULTI_LINE_STYLE)
    }
}