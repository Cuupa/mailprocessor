package com.cuupa.mailprocessor.services.extractors

interface TextExtractor {

    fun extract(content: ByteArray): List<String>
}
