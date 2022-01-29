package com.cuupa.mailprocessor.userconfiguration

import com.fasterxml.jackson.annotation.JsonProperty

class EmailConfiguration {

    @JsonProperty("url")
    var url: String? = null

    @JsonProperty("username")
    var username: String? = null

    @JsonProperty("password")
    var password: String? = null

    @JsonProperty("protocol")
    var protocol: String? = null

    @JsonProperty("labels")
    var labels: List<String>? = null

    @JsonProperty("markasread")
    var markAsRead = false

    @JsonProperty("enabled")
    var enabled = false
}
