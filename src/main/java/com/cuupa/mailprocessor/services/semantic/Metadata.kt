package com.cuupa.mailprocessor.services.semantic

import java.io.Serializable

data class Metadata(val name: String, val value: String) : Serializable {

    override fun toString(): String {
        return "$name: $value"
    }
}