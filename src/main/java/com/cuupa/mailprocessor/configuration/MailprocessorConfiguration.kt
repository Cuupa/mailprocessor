package com.cuupa.mailprocessor.configuration

import com.cuupa.mailprocessor.userconfiguration.UserConfiguration
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class MailprocessorConfiguration {

    @SerializedName("decisiontables")
    @Expose
    var decisiontables: String? = null

    @SerializedName("semantic.url")
    @Expose
    var semanticUrl: String? = null

    @SerializedName("userconfiguration")
    @Expose
    var userConfiguration = listOf<UserConfiguration>()

    val users: List<String> = userConfiguration.filter { !it.username.isNullOrEmpty() }
        .map { it.username!! }

    fun getConfigurationForUser(user: String) = userConfiguration.firstOrNull { it.username == user }
        ?: UserConfiguration()
}
