package com.cuupa.mailprocessor.services.input.scan

import com.cuupa.mailprocessor.services.archive.File
import com.cuupa.mailprocessor.services.archive.FileFactory
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
        FileFactory.getForPath(path).use { file ->
            if (file == null) {
                return listOf()
            }
            file.init(config.username, config.password)
            return getDocuments(file, path, config, user)
        }
    }

    private fun getDocuments(file: File, path: String, config: ScanProperties, user: String): List<PDF> {
        return file.list(path)
            .filter { isCorrectFileType(it, config.fileTypes) }
            .filter { isCorrectScanner(it, config.scannerPrefix) }
            .map { createPdfObject(user, path, file, it) }
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

    fun moveScan(filename: String?, filecontent: ByteArray?, scanProperties: ScanProperties,
                 targetPath: String): Boolean {
        val path = scanProperties.path
        if (filename.isNullOrEmpty() || path.isNullOrEmpty()) {
            return false
        }
        FileFactory.getForPath(path).use { fileProtocol ->
            if (fileProtocol == null) {
                return false
            }

            fileProtocol.init(scanProperties.username, scanProperties.password)
            return moveFile(fileProtocol, path, targetPath, filename, filecontent, scanProperties)
        }
    }

    private fun moveFile(file: File, path: String, targetPath: String, filename: String, content: ByteArray?,
                         properties: ScanProperties): Boolean {
        return if (file.save(file.createDirectories(path, targetPath), filename, content ?: ByteArray(0))) {
            file.delete("${properties.path}/${properties.folder}", filename)
        } else {
            false
        }
    }

    private fun createPdfObject(user: String, path: String, file: File, fileResource: FileResource): PDF {
        val inputStream = file.get(path, fileResource.name)
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

        if (scannerPrefix.size == 1 && scannerPrefix.contains(asterisk)) {
            return true
        }
        return scannerPrefix.find { fileResource.name.startsWith(it) } != null
    }

    private fun isCorrectFileType(fileResource: FileResource, fileTypes: List<String>): Boolean {
        if (fileTypes.isEmpty() || !fileResource.isFile) {
            return false
        }
        if (fileTypes.size == 1 && fileTypes.contains(asterisk)) {
            return true
        }
        return fileTypes.find { fileResource.name.endsWith(it) } != null
    }

    companion object {
        private const val asterisk = "*"
    }
}
