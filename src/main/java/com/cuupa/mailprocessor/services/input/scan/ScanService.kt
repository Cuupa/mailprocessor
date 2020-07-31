package com.cuupa.mailprocessor.services.input.scan

import com.cuupa.mailprocessor.services.archive.FileProtocol
import com.cuupa.mailprocessor.services.archive.FileProtocolFactory
import com.cuupa.mailprocessor.services.archive.FileResource
import com.cuupa.mailprocessor.services.input.Document
import com.cuupa.mailprocessor.services.input.PDF
import com.cuupa.mailprocessor.userconfiguration.ScanProperties
import org.apache.commons.io.IOUtils

class ScanService {

    fun loadScans(user: String, config: ScanProperties): List<Document> {
        if (config.path.isNullOrEmpty()) {
            return listOf()
        }
        val path = getPath(config)
        FileProtocolFactory.getForPath(path).use { fileProtocol ->
            if (fileProtocol == null) {
                return listOf()
            }
            fileProtocol.init(config.username, config.password)
            return fileProtocol.list(path).filter { isCorrectFileType(it, config.fileTypes) }.filter {
                isCorrectScanner(it, config.scannerPrefix)
            }.map { createPdfObject(user, path, fileProtocol, it) }
        }
    }

    private fun getPath(config: ScanProperties): String {
        return if (config.folder.isNullOrEmpty()) {
            getPathWithPort(config)
        } else {
            "${getPathWithPort(config)}/${config.folder}"
        }
    }

    private fun getPathWithPort(config: ScanProperties): String {
        return if (config.port == 0) {
            "${config.path}"
        } else {
            "${config.path}:${config.port}"
        }
    }

    fun moveScan(filename: String?,
                 filecontent: ByteArray?,
                 scanProperties: ScanProperties,
                 targetPath: String): Boolean {
        val path = scanProperties.path
        if (filename.isNullOrEmpty() || path.isNullOrEmpty()) {
            return false
        }
        FileProtocolFactory.getForPath(path).use { fileProtocol ->
            if (fileProtocol == null) {
                return false
            }

            fileProtocol.init(scanProperties.username, scanProperties.password)

            val directories = fileProtocol.createDirectories(path, targetPath)
            return if (fileProtocol.save(directories, filename, filecontent ?: ByteArray(0))) {
                fileProtocol.delete("$path/${scanProperties.path}", filename)
            } else {
                false
            }
        }
    }

    private fun createPdfObject(user: String,
                                path: String,
                                fileProtocol: FileProtocol,
                                fileResource: FileResource): PDF {
        val inputStream = fileProtocol.get(path, fileResource.name)
        val pdf = PDF()
        pdf.content = IOUtils.toByteArray(inputStream)
        pdf.filename = fileResource.name
        pdf.user = user
        return pdf
    }

    private fun isCorrectScanner(fileResource: FileResource, scannerPrefix: List<String>): Boolean {
        if (scannerPrefix.isEmpty()) {
            return false
        }

        if (scannerPrefix.size == 1 && scannerPrefix.contains("*")) {
            return true
        }
        return scannerPrefix.find { fileResource.name.startsWith(it) } != null
    }

    private fun isCorrectFileType(fileResource: FileResource, fileTypes: List<String>): Boolean {
        if (fileTypes.isEmpty() || !fileResource.isFile) {
            return false
        }
        if (fileTypes.size == 1 && fileTypes.contains("*")) {
            return true
        }
        return fileTypes.find { fileResource.name.endsWith(it) } != null
    }
}
