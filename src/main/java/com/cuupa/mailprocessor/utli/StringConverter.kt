package com.cuupa.mailprocessor.utli

import java.nio.charset.Charset
import java.nio.charset.StandardCharsets

class StringConverter {

    fun convertTo(string: String, charset: Charset): String {
        val sourceCharset = charset(string, charsets)
        val byteArray = string.toByteArray(sourceCharset)
        return String(byteArray, charset)
    }

    private fun charset(value: String, charsets: Array<String>): Charset {
        val probe = StandardCharsets.UTF_8
        for (c in charsets) {
            val charset: Charset = Charset.forName(c)
            if (value == convert(convert(value, charset.name(), probe), probe.name(), charset)) {
                return charset
            }
        }
        return StandardCharsets.UTF_8
    }

    private fun convert(value: String, fromEncoding: String, toEncoding: Charset): String {
        return String(value.toByteArray(charset(fromEncoding)), toEncoding)
    }

    companion object {
        val charsets = arrayOf("ISO-8859-1", "UTF-8")
    }
}
