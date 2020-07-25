package com.cuupa.mailprocessor.userconfiguration

import com.google.common.base.Strings
import java.io.Serializable

open class ConfigurationProperties : Serializable {

    protected fun getPasswordStared(password: String?): String {
        return if (password.isNullOrEmpty()) {
            ""
        } else Strings.repeat("*", password.toCharArray().size)
    }
}