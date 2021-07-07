package com.cuupa.mailprocessor

import com.cuupa.mailprocessor.configuration.MailprocessorConfiguration
import com.cuupa.mailprocessor.userconfiguration.UserConfiguration
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

object MailprocessorConfigurationLoader {

    fun load(configpath: String): MailprocessorConfiguration {
        return gson.fromJson(Files.readString(Paths.get(configpath)), listType)
    }

    private val listType: Type = object : TypeToken<ArrayList<UserConfiguration?>?>() {}.type
    private val gson = Gson()
}