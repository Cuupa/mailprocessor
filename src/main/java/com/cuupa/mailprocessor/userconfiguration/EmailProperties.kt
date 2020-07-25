package com.cuupa.mailprocessor.userconfiguration

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import org.apache.commons.lang3.builder.ToStringBuilder

class EmailProperties : ConfigurationProperties() {
    @SerializedName("servername")
    @Expose
    var servername: String? = null

    @SerializedName("port")
    @Expose
    var port = 0

    @SerializedName("username")
    @Expose
    var username: String? = null

    @SerializedName("password")
    @Expose
    var password: String? = null

    @SerializedName("protocol")
    @Expose
    var protocol: String? = null

    @SerializedName("labels")
    @Expose
    var labels: List<String>? = null

    @SerializedName("markasread")
    @Expose
    var isMarkAsRead = false

    @SerializedName("enabled")
    @Expose
    var isEnabled = false

    fun servername(servername: String?): EmailProperties {
        this.servername = servername
        return this
    }

    fun port(port: Int): EmailProperties {
        this.port = port
        return this
    }

    fun username(username: String?): EmailProperties {
        this.username = username
        return this
    }

    fun password(password: String?): EmailProperties {
        this.password = password
        return this
    }

    fun protocol(protocol: String?): EmailProperties {
        this.protocol = protocol
        return this
    }

    fun labels(labels: List<String>): EmailProperties {
        this.labels = labels
        return this
    }

    fun markAsRead(markAsRead: Boolean): EmailProperties {
        isMarkAsRead = markAsRead
        return this
    }

    fun enabled(enabled: Boolean): EmailProperties {
        isEnabled = enabled
        return this
    }

    override fun toString(): String {
        return ToStringBuilder(this).append("servername", servername)
                .append("port", port)
                .append("username", username)
                .append("password", getPasswordStared(password!!))
                .append("protocol", protocol)
                .append("labels", labels)
                .append("markasread", isMarkAsRead)
                .append("enabled", isEnabled)
                .toString()
    }
}