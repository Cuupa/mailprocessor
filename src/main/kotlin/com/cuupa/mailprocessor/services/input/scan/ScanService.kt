package com.cuupa.mailprocessor.services.input.scan

import com.cuupa.mailprocessor.services.files.transfer.File
import com.cuupa.mailprocessor.services.files.transfer.FileFactory
import com.cuupa.mailprocessor.services.files.transfer.FileResource
import com.cuupa.mailprocessor.services.input.Document
import com.cuupa.mailprocessor.userconfiguration.DirectoryConfiguration
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.apache.commons.io.IOUtils

class ScanService {

    fun loadScans(user: String, config: DirectoryConfiguration): List<Document> {

        config.path?.let { path ->
            FileFactory.getForPath(path).use { file ->
                file.init(config.username, config.password)
                return getDocuments(file, path, config, user)
            }
        }

        return listOf()
    }

    private fun getDocuments(file: File, path: String, config: DirectoryConfiguration, user: String): List<Document> {
        return runBlocking(Dispatchers.Default) {
            return@runBlocking file.list(path)
                .filter { isCorrectFileType(it, config.filetypes!!) }
                .filter { isCorrectScanner(it, config.prefix!!) }
                .map { createObject(user, path, file, it) }
        }
    }

    fun moveScan(
        filename: String?, filecontent: ByteArray?, targetPath: String,
        scanProperties: DirectoryConfiguration
    ): Boolean {

        scanProperties.path?.let { path ->
            if (path.isEmpty()) {
                return false
            }
            FileFactory.getForPath(path).use { file ->
                file.init(scanProperties.username, scanProperties.password)
                return moveFile(file, path, targetPath, filename!!, filecontent, scanProperties)
            }
        }
        return false
    }

    private fun moveFile(
        file: File, path: String, targetPath: String, filename: String, content: ByteArray?,
        properties: DirectoryConfiguration
    ): Boolean {
        return if (file.save(file.createDirectories(path, targetPath), filename, content ?: ByteArray(0))) {
            file.delete("${properties.path}", filename)
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
