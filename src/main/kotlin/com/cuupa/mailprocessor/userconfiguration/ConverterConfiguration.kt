package com.cuupa.mailprocessor.userconfiguration

import com.fasterxml.jackson.annotation.JsonProperty

class ConverterConfiguration {

    @JsonProperty("url")
    var url: String? = null

    @JsonProperty("enabled")
    var enabled: Boolean?= null
}
