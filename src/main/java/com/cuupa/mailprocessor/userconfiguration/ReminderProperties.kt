package com.cuupa.mailprocessor.userconfiguration

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import org.apache.commons.lang3.builder.ToStringBuilder

class ReminderProperties : ConfigurationProperties() {
    @SerializedName("botname")
    @Expose
    var botname: String? = null

    @SerializedName("token")
    @Expose
    var token: String? = null

    @SerializedName("chatId")
    @Expose
    var chatId: String? = null

    @SerializedName("url")
    @Expose
    var url: String? = null

    @SerializedName("enabled")
    @Expose
    var isEnabled = false

    fun botname(botname: String?): ReminderProperties {
        this.botname = botname
        return this
    }

    fun token(token: String?): ReminderProperties {
        this.token = token
        return this
    }

    fun chatId(chatId: String?): ReminderProperties {
        this.chatId = chatId
        return this
    }

    fun url(url: String?): ReminderProperties {
        this.url = url
        return this
    }

    fun enabled(enabled: Boolean): ReminderProperties {
        isEnabled = enabled
        return this
    }

    override fun toString(): String {
        return ToStringBuilder(this).append("botname", botname)
                .append("token", getPasswordStared(token!!))
                .append("chatId", chatId)
                .append("url", url)
                .append("enabled", isEnabled)
                .toString()
    }
}