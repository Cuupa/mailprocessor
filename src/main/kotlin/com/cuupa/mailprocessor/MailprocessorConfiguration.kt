package com.cuupa.mailprocessor

import com.cuupa.mailprocessor.userconfiguration.Configuration
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import java.io.File

class MailprocessorConfiguration(configpath: String) {

    val configurations: Configuration = getObjectMapper().readValue(File(configpath), Configuration::class.java)

    fun getObjectMapper(): ObjectMapper {
        return ObjectMapper(YAMLFactory()).apply {
            enable(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT)
            disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        }
    }
}