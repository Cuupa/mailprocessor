package com.cuupa.mailprocessor.userconfiguration

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import org.apache.commons.lang3.builder.ToStringBuilder
import java.util.*

class UserConfiguration {

    @SerializedName("username")
    @Expose
    var username: String? = null

    @SerializedName("locale")
    @Expose
    var locale: Locale = Locale.getDefault()

    @SerializedName("emailproperties")
    @Expose
    var emailProperties = EmailProperties()

    @SerializedName("scanproperties")
    @Expose
    var scanProperties = ScanProperties()

    @SerializedName("archiveproperties")
    @Expose
    var archiveProperties = ArchiveProperties()

    @SerializedName("reminderproperties")
    @Expose
    var reminderProperties = ReminderProperties()

    @SerializedName("decisiontable")
    @Expose
    var decisiontableProperties = DecisiontableProperties()

    override fun toString(): String {
        return ToStringBuilder(this).append("username", username)
            .append("locale", locale)
            .append("emailproperties", emailProperties)
            .append("scanproperties", scanProperties)
            .append("archiveproperties", archiveProperties)
            .append("reminderproperties", reminderProperties)
            .toString()
    }
}