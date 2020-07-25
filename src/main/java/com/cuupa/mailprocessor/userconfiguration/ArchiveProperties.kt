package com.cuupa.mailprocessor.userconfiguration

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import org.apache.commons.lang3.builder.ToStringBuilder

class ArchiveProperties : ConfigurationProperties() {
    @SerializedName("path")
    @Expose
    var path: String? = null

    @SerializedName("port")
    @Expose
    var port = 0

    @SerializedName("username")
    @Expose
    var username: String? = null

    @SerializedName("password")
    @Expose
    var password: String? = null

    fun path(path: String?): ArchiveProperties {
        this.path = path
        return this
    }

    fun port(port: Int): ArchiveProperties {
        this.port = port
        return this
    }

    fun username(username: String?): ArchiveProperties {
        this.username = username
        return this
    }

    fun password(password: String?): ArchiveProperties {
        this.password = password
        return this
    }

    override fun toString(): String {
        return ToStringBuilder(this).append("path", path)
                .append("port", port)
                .append("username", username)
                .append("password", getPasswordStared(password))
                .toString()
    }
}