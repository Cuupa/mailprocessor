package com.cuupa.mailprocessor.configuration

import com.cuupa.mailprocessor.delegates.reminder.DetectReminder
import com.cuupa.mailprocessor.delegates.reminder.TriggerReminderDelegate
import com.cuupa.mailprocessor.services.reminder.ReminderService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class ReminderDelegateConfiguration {

    @Autowired
    private var mailprocessorConfiguration: MailprocessorConfiguration? = null

    @Autowired
    private var reminderService: ReminderService? = null

    @Bean
    open fun detectReminderDelegate(): DetectReminder {
        return DetectReminder(mailprocessorConfiguration!!)
    }

    @Bean
    open fun triggerReminderDelegate(): TriggerReminderDelegate {
        return TriggerReminderDelegate(reminderService!!, mailprocessorConfiguration!!)
    }
}
