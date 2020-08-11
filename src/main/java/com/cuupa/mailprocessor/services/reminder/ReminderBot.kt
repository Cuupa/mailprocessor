package com.cuupa.mailprocessor.services.reminder

import com.cuupa.mailprocessor.userconfiguration.ReminderProperties
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.objects.Update

class ReminderBot(private val properties: ReminderProperties) : TelegramLongPollingBot() {
    override fun getBotUsername(): String = properties.botname ?: ""
    override fun getBotToken(): String = properties.token ?: ""

    override fun onUpdateReceived(p0: Update?) {
        if (p0?.callbackQuery?.data.isNullOrEmpty()) {
            TODO("Not yet implemented")
        }
    }
}
