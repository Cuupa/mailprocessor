package com.cuupa.mailprocessor.services

import com.cuupa.mailprocessor.services.semantic.Metadata
import com.cuupa.mailprocessor.userconfiguration.ReminderProperties
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.URL
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ReminderService {

    fun remind(fileName: String,
               reminderDate: String,
               metadata: List<Metadata>,
               reminderProperties: ReminderProperties) {
        val format = LocalDateTime.parse(reminderDate).toLocalDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
        val text = URLEncoder.encode(getTextForReminder(fileName, format, metadata), StandardCharsets.UTF_8)
        val url = "${reminderProperties.url}${reminderProperties.token}/sendMessage?chat_id=${reminderProperties.chatId}&text=$text"
        callReminder(url)
    }

    private fun callReminder(url: String) {
        val openConnection = URL(url).openConnection()
        val sb = StringBuilder()
        val `is`: InputStream = BufferedInputStream(openConnection.getInputStream())
        val br = BufferedReader(InputStreamReader(`is`))
        var inputLine: String?
        while (br.readLine().also { inputLine = it } != null) {
            sb.append(inputLine)
        }
        val response = sb.toString()
        print(response)
    }

    private fun getTextForReminder(fileName: String, reminderDate: String, metadata: List<Metadata>): String {
        return metadata.joinToString("\n",
                                     "The mail $fileName needs your attention until $reminderDate \n",
                                     "") { it.toString() }
    }

}
