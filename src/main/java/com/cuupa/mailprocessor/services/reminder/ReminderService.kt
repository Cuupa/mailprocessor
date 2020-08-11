package com.cuupa.mailprocessor.services.reminder

import com.cuupa.mailprocessor.services.semantic.Metadata
import com.cuupa.mailprocessor.userconfiguration.ReminderProperties
import org.apache.juli.logging.LogFactory
import org.telegram.telegrambots.ApiContextInitializer
import org.telegram.telegrambots.meta.TelegramBotsApi
import org.telegram.telegrambots.meta.api.methods.send.SendDocument
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.InputFile
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton
import org.telegram.telegrambots.meta.exceptions.TelegramApiException
import java.io.ByteArrayInputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ReminderService {

    fun remind(processInstanceId: String, fileName: String, reminderDate: String, metadata: List<Metadata>,
               content: ByteArray, reminderProperties: ReminderProperties) {
        executeReminder(processInstanceId, reminderProperties, fileName, reminderDate, metadata, content)
    }

    fun executeReminder(processInstanceId: String, reminderProperties: ReminderProperties, fileName: String,
                        reminderDate: String, metadata: List<Metadata>, content: ByteArray) {
        if (!reminderProperties.isEnabled) {
            return
        }

        val botsApi = TelegramBotsApi()
        val reminderBot = ReminderBot(reminderProperties)
        try {
            botsApi.registerBot(reminderBot)
        } catch (e: TelegramApiException) {
            log.error("Error creating Telegram reminder for user", e)
            return
        }

        val message = createMessage(reminderProperties, fileName, reminderDate, metadata)
        reminderBot.execute(message)

        val document = createCallbackAndButton(processInstanceId, createDocument(reminderProperties, content, fileName))
        reminderBot.execute(document)
    }

    private fun createCallbackAndButton(processInstanceId: String, document: SendDocument): SendDocument {
        val rowInline = listOf(InlineKeyboardButton().setText("Confirm").setCallbackData(processInstanceId))
        val markupInline = InlineKeyboardMarkup()
        markupInline.keyboard = listOf(rowInline)
        document.replyMarkup = markupInline
        return document
    }

    private fun createDocument(reminderProperties: ReminderProperties, content: ByteArray,
                               fileName: String) = SendDocument().setChatId(reminderProperties.chatId)
        .setDocument(InputFile(ByteArrayInputStream(content), fileName))

    private fun createMessage(reminderProperties: ReminderProperties, fileName: String, reminderDate: String,
                              metadata: List<Metadata>): SendMessage? {
        val dateString = LocalDateTime.parse(reminderDate).toLocalDate().format(formatter)
        return SendMessage().setChatId(reminderProperties.chatId)
            .setText(getTextForReminder(fileName, dateString, metadata))
    }

    private fun getTextForReminder(fileName: String, reminderDate: String, metadata: List<Metadata>): String {
        return metadata.joinToString("\n",
                                     "The mail \"$fileName\" needs your attention until $reminderDate \n",
                                     "") { it.toString() }
    }

    init {
        ApiContextInitializer.init()
    }

    companion object {
        private val log = LogFactory.getLog(ReminderService::class.java)
        private val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
    }
}
