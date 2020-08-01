package com.cuupa.mailprocessor.services.input.scan

import com.cuupa.mailprocessor.services.extractors.ZipExtractor
import com.cuupa.mailprocessor.services.files.File
import com.cuupa.mailprocessor.services.files.FileFactory
import com.cuupa.mailprocessor.services.files.FileResource
import com.cuupa.mailprocessor.services.input.Document
import com.cuupa.mailprocessor.services.input.Zip
import com.cuupa.mailprocessor.userconfiguration.ScanProperties
import org.apache.commons.io.IOUtils

class ScanService {

    fun loadScans(user: String, config: ScanProperties): List<Document> {
        if (config.path.isNullOrEmpty()) {
            return listOf()
        }
        val path = getPath(config)
        FileFactory.getForPath(path).use { file ->
            file.init(config.username, config.password)
            return getDocuments(file, path, config, user).map { expandZipFiles(it) }.flatten()
        }
    }

    private fun getDocuments(file: File, path: String, config: ScanProperties, user: String): List<Document> {
        return file.list(path)
            .filter { isCorrectFileType(it, config.fileTypes) }
            .filter { isCorrectScanner(it, config.scannerPrefix) }
            .map { createObject(user, path, file, it) }
    }

    private fun getPath(config: ScanProperties): String {
        return if (config.folder.isNullOrEmpty()) {
            getPathWithPort(config)
        } else {
            "${getPathWithPort(config)}/${config.folder}"
        }
    }

    private fun getPathWithPort(config: ScanProperties): String {
        return when (config.port) {
            0 -> "${config.path}"
            else -> "${config.path}:${config.port}"
        }
    }

    fun moveScan(filename: String?, filecontent: ByteArray?, scanProperties: ScanProperties,
                 targetPath: String): Boolean {
        val path = scanProperties.path
        if (filename.isNullOrEmpty() || path.isNullOrEmpty()) {
            return false
        }
        FileFactory.getForPath(path).use { file ->
            file.init(scanProperties.username, scanProperties.password)
            return moveFile(file, path, targetPath, filename, filecontent, scanProperties)
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

    private fun createObject(user: String, path: String, file: File, fileResource: FileResource): Document {
        val document = Documents.get(IOUtils.toByteArray(file.get(path, fileResource.name)))
        document.filename = fileResource.name
        document.user = user
        return document
    }

    private fun expandZipFiles(document: Document): List<Document> {
        return when (document) {
            !is Zip -> listOf(document)
            else -> expand(document)
        }
    }

    private fun expand(zip: Zip): List<Document> {
        val documents = ZipExtractor().extract(zip.content!!)
        documents.forEach { it.user = zip.user }
        documents.forEach { it.originalFileName = zip.filename }
        return documents
    }

    private fun isCorrectScanner(fileResource: FileResource, scannerPrefix: List<String>): Boolean {
        return when {
            scannerPrefix.isEmpty() -> false
            scannerPrefix.size == 1 && scannerPrefix.contains(asterisk) -> true
            else -> scannerPrefix.find { fileResource.name.startsWith(it) } != null
        }
    }

    private fun isCorrectFileType(fileResource: FileResource, fileTypes: List<String>): Boolean {
        return when {
            fileTypes.isEmpty() || !fileResource.isFile -> false
            fileTypes.size == 1 && fileTypes.contains(asterisk) -> true
            else -> fileTypes.find { fileResource.name.endsWith(it) } != null
        }
    }

    companion object {
        private const val asterisk = "*"
    }
}
