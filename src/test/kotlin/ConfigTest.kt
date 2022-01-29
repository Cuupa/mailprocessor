package com.cuupa.mailprocessor.configuration

import com.cuupa.mailprocessor.userconfiguration.Configuration
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import java.io.File

class ConfigTest {

    @Test
    fun shouldLoadConfig() {
        val config = getObjectMapper().readValue(
            File("/Users/simon/IdeaProjects/mailprocessor/docker/configuration.yml"),
            Configuration::class.java)

        assertNotNull(config)
    }

    fun getObjectMapper(): ObjectMapper {
        return ObjectMapper(YAMLFactory()).apply {
            enable(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT)
            disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        }
    }
}