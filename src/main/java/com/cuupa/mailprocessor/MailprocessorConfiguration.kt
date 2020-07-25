package com.cuupa.mailprocessor

import com.cuupa.mailprocessor.userconfiguration.UserConfiguration
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.apache.juli.logging.LogFactory
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

class MailprocessorConfiguration(configpath: String?) {
    val configurations: MutableList<UserConfiguration> = ArrayList()

    val users: List<String>
        get() = configurations.filter { config -> !config.username.isNullOrEmpty() }.map { it -> it.username!! }

    fun getConfigurationForUser(user: String): UserConfiguration {
        return configurations.stream()
                .filter { config: UserConfiguration -> config.username == user }
                .findAny()
                .orElse(UserConfiguration())
    }

    companion object {
        private val LOG = LogFactory.getLog(MailprocessorConfiguration::class.java)
    }

    init {
        try {
            val listType = object : TypeToken<ArrayList<UserConfiguration?>?>() {}.type
            configurations.addAll(Gson()
                    .fromJson(Files.readString(Paths.get(configpath)), listType))
        } catch (ioException: IOException) {
            LOG.error("Couldn't read config file", ioException)
        }
    }
}