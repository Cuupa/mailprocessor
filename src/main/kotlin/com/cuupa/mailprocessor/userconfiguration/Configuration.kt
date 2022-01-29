package com.cuupa.mailprocessor.userconfiguration

import com.fasterxml.jackson.annotation.JsonProperty

class Configuration {

    @JsonProperty("configuration")
    var entries: List<UserConfiguration>? = null
}