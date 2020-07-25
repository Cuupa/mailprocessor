package com.cuupa.mailprocessor.services.input

import com.cuupa.mailprocessor.services.archive.ArchiveResource
import com.cuupa.mailprocessor.services.archive.FileProtocol
import com.cuupa.mailprocessor.services.archive.FileProtocolFactory
import com.cuupa.mailprocessor.userconfiguration.ScanProperties
import org.apache.commons.io.IOUtils

class ScanService {

    fun loadScans(user: String, config: ScanProperties): List<Document> {
        if (config.path.isNullOrEmpty()) {
            return listOf()
        }
        val path = config.path!!
        FileProtocolFactory.getForPath(path).use { fileProtocol ->
            if (fileProtocol == null) {
                return listOf()
            }
            fileProtocol.init(config.username, config.password)
            return fileProtocol.list(path).filter { it.isPdf }.filter {
                isCorrectScanner(it, config
                        .scannerPrefix)
            }.map { createPdfObject(user, path, fileProtocol, it) }
        }
    }

    private fun createPdfObject(user: String, path: String, fileProtocol: FileProtocol,
                                archiveResource: ArchiveResource): PDF {
        val inputStream = fileProtocol.get(path, archiveResource.name)
        val pdf = PDF()
        pdf.content = IOUtils.toByteArray(inputStream)
        pdf.filename = archiveResource.name
        pdf.user = user
        return pdf
    }

    private fun isCorrectScanner(archiveResource: ArchiveResource, scannerPrefix: List<String>): Boolean {
        if (scannerPrefix.isEmpty()) {
            return false
        }

        if (scannerPrefix.size == 1 && scannerPrefix.contains("*")) {
            return true
        }
        return scannerPrefix.find { archiveResource.name.startsWith(it) } != null
    }
}
