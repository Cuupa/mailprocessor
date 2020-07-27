package com.cuupa.mailprocessor.services.input

import java.io.Serializable

open class Document : Serializable {
    lateinit var user: String
    lateinit var filename: String
    var content: ByteArray? = null
}
