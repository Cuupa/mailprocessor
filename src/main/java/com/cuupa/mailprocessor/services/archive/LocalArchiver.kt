package com.cuupa.mailprocessor.services.archive

import java.io.ByteArrayInputStream
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*
import java.util.stream.Collectors

class LocalArchiver : FileProtocol {

    override fun init(username: String?, password: String?) {
        // Not implemented
    }

    override fun exists(path: String): Boolean {
        return Files.exists(Paths.get(path))
    }

    override fun save(path: String, data: ByteArray): Boolean {
        return try {
            Files.copy(ByteArrayInputStream(data), Paths.get(path))
            true
        } catch (e: IOException) {
            false
        }
    }

    override fun createDirectory(path: String): Boolean {
        return try {
            Files.createDirectory(Paths.get(path))
            true
        } catch (e: IOException) {
            false
        }
    }

    override fun list(path: String): List<ArchiveResource> {
        return try {
            Files.list(Paths.get(path))
                    .map { e: Path ->
                        ArchiveResource(e.fileName.toString(), getContentType(e))
                    }
                    .collect(Collectors.toList())
        } catch (e: IOException) {
            ArrayList()
        }
    }

    private fun getContentType(e: Path): String {
        return try {
            Files.probeContentType(e)
        } catch (ioException: IOException) {
            "*/*"
        }
    }

    override fun close() {
        // Not implemented
    }
}