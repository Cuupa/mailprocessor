package com.cuupa.mailprocessor.userconfiguration

import com.fasterxml.jackson.annotation.JsonProperty

class OutputConfiguration {

    @JsonProperty("path")
    var path: String? = null

    @JsonProperty("username")
    var username: String? = null

    @JsonProperty("password")
    var password: String? = null
}
