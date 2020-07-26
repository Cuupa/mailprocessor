package com.cuupa.mailprocessor.services

import com.cuupa.mailprocessor.services.semantic.Metadata
import com.cuupa.mailprocessor.userconfiguration.ReminderProperties
import java.net.URL
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

class ReminderService {

    fun remind(fileName: String,
               reminderDate: String,
               metadata: List<Metadata>,
               reminderProperties: ReminderProperties) {
        val text = URLEncoder.encode(getTextForReminder(fileName, reminderDate, metadata), StandardCharsets.UTF_8)
        val url = "${reminderProperties.url}${reminderProperties.token}/sendMessage?chat_id=${reminderProperties.chatId}&text = $text"
        callReminder(url)
    }

    private fun callReminder(url: String) {
        URL(url).openConnection()
    }

    private fun getTextForReminder(fileName: String, reminderDate: String, metadata: List<Metadata>): String {
        return metadata.joinToString("\n",
                                     "The mail $fileName needs your attention until $reminderDate \n",
                                     "") { it.toString() }
    }

}
