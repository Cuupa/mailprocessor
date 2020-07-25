package com.cuupa.mailprocessor.services.input

open class Document {
    lateinit var user: String
    lateinit var filename: String
    var content: ByteArray? = null
}
