package com.cuupa.mailprocessor.delegates.reminder

import com.cuupa.mailprocessor.configuration.MailprocessorConfiguration
import com.cuupa.mailprocessor.process.ProcessInstanceHandler
import com.cuupa.mailprocessor.services.reminder.ReminderService
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.engine.delegate.JavaDelegate

class TriggerReminderDelegate(
    private val reminderService: ReminderService,
    private val config: MailprocessorConfiguration
) : JavaDelegate {

    override fun execute(delegateExecution: DelegateExecution) {
        val handler = ProcessInstanceHandler(delegateExecution)
        val reminderProperties = config.getConfigurationForUser(handler.username).reminderProperties
        reminderService.remind(
            handler.processInstanceId,
            handler.fileName,
            handler.reminderDate!!,
            handler.metadata,
            handler.fileContent,
            reminderProperties
        )
        handler.hasReminder = false
    }
}