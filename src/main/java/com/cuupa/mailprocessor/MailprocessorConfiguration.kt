package com.cuupa.mailprocessor

import com.cuupa.mailprocessor.userconfiguration.UserConfiguration
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

class MailprocessorConfiguration(configpath: String) {

    val configurations: List<UserConfiguration> = gson.fromJson(Files.readString(Paths.get(configpath)), listType)

    val users: List<String> = configurations.filter { !it.username.isNullOrEmpty() }
        .map { it.username!! }

    fun getConfigurationForUser(user: String) = configurations.firstOrNull { it.username == user }
            ?: UserConfiguration()

    companion object {
        private val listType: Type = object : TypeToken<ArrayList<UserConfiguration?>?>() {}.type
        private val gson = Gson()
    }
}