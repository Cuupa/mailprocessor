package com.cuupa.mailprocessor.userconfiguration

import com.fasterxml.jackson.annotation.JsonProperty

class OcrConfiguration {

    @JsonProperty("input")
    var input: String? = null

    @JsonProperty("output")
    var output: String? = null

    @JsonProperty("username")
    var username: String? = null

    @JsonProperty("password")
    var password: String? = null

    @JsonProperty("enabled")
    var enabled: Boolean? = null
}
