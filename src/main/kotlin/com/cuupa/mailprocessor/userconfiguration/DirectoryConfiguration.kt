package com.cuupa.mailprocessor.userconfiguration

import com.fasterxml.jackson.annotation.JsonProperty

class DirectoryConfiguration {

    @JsonProperty("path")
    var path: String? = null

    @JsonProperty("error")
    var error: String? = null

    @JsonProperty("success")
    var success: String? = null

    @JsonProperty("username")
    var username: String? = null

    @JsonProperty("password")
    var password: String? = null

    @JsonProperty("prefix")
    var prefix: List<String>? = null
        get() = field ?: listOf()

    @JsonProperty("filetypes")
    var filetypes: List<String>? = null
        get() = field ?: listOf()

    @JsonProperty("enabled")
    var enabled = false
}
