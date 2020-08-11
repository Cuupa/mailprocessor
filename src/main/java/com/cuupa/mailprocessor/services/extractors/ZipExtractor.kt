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
            var entry = inputStream.nextEntry
            var currentDirectoryName = ""
            while (entry != null) {
                if (entry.isDirectory) {
                    currentDirectoryName = entry.name
                    inputStream.closeEntry()
                    entry = inputStream.nextEntry
                    continue
                }
                ByteArrayOutputStream().use { outputStream ->
                    var len: Int
                    while (inputStream.read(buffer).also { len = it } > 0) {
                        outputStream.write(buffer, 0, len)
                    }
                    val document = Zip(outputStream.toByteArray())
                    document.filename = getFilename(entry, currentDirectoryName)
                    value.add(document)
                }
                inputStream.closeEntry()
                entry = inputStream.nextEntry
            }
        }
        return value
    }

    private fun getFilename(entry: ZipEntry, currentDirectoryName: String): String {
        return if(entry.name.contains(currentDirectoryName)){
            entry.name.split(currentDirectoryName.toRegex()).last()
        } else {
            entry.name
        }
    }
}
