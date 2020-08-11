package com.cuupa.mailprocessor.services.input

class Zip(content: ByteArray) : Document(content) {

    lateinit var originalFileName: String
}
