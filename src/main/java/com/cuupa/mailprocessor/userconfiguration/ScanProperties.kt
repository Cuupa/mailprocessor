package com.cuupa.mailprocessor.userconfiguration

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import org.apache.commons.lang3.builder.ToStringBuilder

class ScanProperties : ConfigurationProperties() {
    @SerializedName("path")
    @Expose
    var root: String? = null

    @SerializedName("folder")
    @Expose
    var workFolder: String? = null
        get() = field ?: ""

    @SerializedName("port")
    @Expose
    var port = 0

    @SerializedName("errorfolder")
    @Expose
    var errorFolder: String = ""

    @SerializedName("successfolder")
    @Expose
    var successFolder: String = ""

    @SerializedName("username")
    @Expose
    var username: String = ""

    @SerializedName("password")
    @Expose
    var password: String? = null

    @SerializedName("scannerprefix")
    @Expose
    var scannerPrefix: List<String> = listOf()

    @SerializedName("filetypes")
    @Expose
    var fileTypes: List<String> = listOf()

    @SerializedName("enabled")
    @Expose
    var isEnabled = false

    fun path(path: String?): ScanProperties {
        this.root = path
        return this
    }

    fun port(port: Int): ScanProperties {
        this.port = port
        return this
    }

    fun username(username: String): ScanProperties {
        this.username = username
        return this
    }

    fun password(password: String?): ScanProperties {
        this.password = password
        return this
    }

    override fun toString(): String {
        return ToStringBuilder(this).append("path", root)
            .append("port", port)
            .append("username", username)
            .append("password", getPasswordStared(password!!))
            .append("scannerprefix", scannerPrefix)
            .append("filetypes", fileTypes)
            .append("enabled", isEnabled)
            .toString()
    }
}