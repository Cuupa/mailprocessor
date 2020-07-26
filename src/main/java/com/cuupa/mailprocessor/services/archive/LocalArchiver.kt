package com.cuupa.mailprocessor.services.archive

import java.io.ByteArrayInputStream
import java.io.IOException
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*
import java.util.stream.Collectors

class LocalArchiver : FileProtocol {

    override fun init(username: String?, password: String?) {
        // Not implemented
    }

    override fun exists(path: String, filename: String): Boolean {
        return Files.exists(Paths.get(path + filename))
    }

    override fun save(path: String, filename: String, data: ByteArray): Boolean {
        return try {
            Files.copy(ByteArrayInputStream(data), Paths.get(path + filename))
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
                    }.collect(Collectors.toList())
        } catch (e: IOException) {
            ArrayList()
        }
    }

    override fun get(name: String, path: String): InputStream {
        TODO("Not yet implemented")
    }

    override fun delete(path: String, filename: String): Boolean {
        TODO("Not yet implemented")
    }

    override fun createDirectories(url: String, path: String): String {
        TODO("Not yet implemented")
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