package com.cuupa.mailprocessor.userconfiguration

import com.fasterxml.jackson.annotation.JsonProperty

class InputConfiguration {

    @JsonProperty("email")
    var email: EmailConfiguration? = null

    @JsonProperty("directory")
    var directory: DirectoryConfiguration? = null

}
