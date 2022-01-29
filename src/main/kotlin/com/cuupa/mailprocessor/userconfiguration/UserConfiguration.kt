package com.cuupa.mailprocessor.userconfiguration

import com.fasterxml.jackson.annotation.JsonProperty

class UserConfiguration {

    @JsonProperty("username")
    var username: String? = null

    @JsonProperty("locale")
    var locale: String? = null

    @JsonProperty("input")
    var input: InputConfiguration? = null

    @JsonProperty("output")
    var output: OutputConfiguration? = null
}
