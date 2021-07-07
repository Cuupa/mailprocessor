package com.cuupa.mailprocessor.userconfiguration

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import org.apache.commons.lang3.builder.ToStringBuilder

class DecisiontableProperties : ConfigurationProperties() {

    @SerializedName("path")
    @Expose
    var pathToDmn: String? = null

    override fun toString(): String {
        return ToStringBuilder(this).append("pathToDmn", pathToDmn)
            .toString()
    }
}
