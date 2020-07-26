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
        if (handler.hasReminder || !configuration.getConfigurationForUser(handler.username).reminderProperties.isEnabled) {
            return
        }

        if (handler.metadata.isEmpty()) {
            handler.hasReminder = false
        } else {
            val dueDate = handler.metadata.find { it.name == "dueDate" }
            if (dueDate != null) {
                handler.hasReminder = true
                val toInstant = LocalDateTime.of(LocalDate.parse(dueDate.value, formatter), LocalTime.of(8, 0))
                handler.reminderDate = DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(toInstant)
            } else {
                handler.hasReminder = false
            }
        }
    }
}