package com.cuupa.mailprocessor.userconfiguration

import com.fasterxml.jackson.annotation.JsonProperty

class ClassificatorConfiguration {

    @JsonProperty("url")
    var url: String? = null

    @JsonProperty("enabled")
    var enabled: Boolean? = null

    @JsonProperty("api-key")
    var apiKey: String? = null
}
