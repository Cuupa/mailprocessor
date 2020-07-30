package com.cuupa.mailprocessor.delegates.reminder

import com.cuupa.mailprocessor.MailprocessorConfiguration
import com.cuupa.mailprocessor.process.ProcessInstanceHandler
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.engine.delegate.JavaDelegate
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class DetectReminder(private val configuration: MailprocessorConfiguration) : JavaDelegate {

    private val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")

    override fun execute(delegateExecution: DelegateExecution) {
        val handler = ProcessInstanceHandler(delegateExecution)
        if (!configuration.getConfigurationForUser(handler.username).reminderProperties.isEnabled) {
            handler.hasReminder = false
            return
        }

        val dueDate = handler.metadata.find { it.value == "dueDate" }
        if (dueDate != null) {
            val toInstant = LocalDateTime.of(LocalDate.parse(dueDate.value, formatter), LocalTime.of(8, 0))
            if(toInstant.isAfter(LocalDateTime.now())) {
                handler.hasReminder = true
                handler.reminderDate = DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(toInstant)
            } else {
                handler.hasReminder = false
            }

        } else if (handler.reminderDate.isNullOrEmpty() && handler.hasReminder) {
            handler.reminderDate = DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(LocalDateTime.now())
        }
    }
}