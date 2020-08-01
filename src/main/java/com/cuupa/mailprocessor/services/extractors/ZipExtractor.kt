package com.cuupa.mailprocessor.services.extractors

import com.cuupa.mailprocessor.services.input.Zip
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream

class ZipExtractor {

    fun extract(content: ByteArray): List<Zip> {
        val value = mutableListOf<Zip>()
        ZipInputStream(ByteArrayInputStream(content)).use { inputStream ->
            val buffer = ByteArray(1024)
            var entry: ZipEntry? = inputStream.nextEntry
            while (entry != null) {
                if (!entry.isDirectory) {
                    ByteArrayOutputStream().use { outputStream ->
                        var len: Int
                        while (inputStream.read(buffer).also { len = it } > 0) {
                            outputStream.write(buffer, 0, len)
                        }
                        val document = Zip(outputStream.toByteArray())
                        document.filename = entry!!.name
                        value.add(document)
                    }
                }
                inputStream.closeEntry()
                entry = inputStream.nextEntry
            }
        }
        return value
    }
}
