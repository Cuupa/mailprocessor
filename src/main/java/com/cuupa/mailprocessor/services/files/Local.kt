package com.cuupa.mailprocessor.services.files

import java.io.ByteArrayInputStream
import java.io.IOException
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.streams.toList

class Local : File {

    override fun init(username: String?, password: String?) {
        // Not implemented
    }

    override fun exists(path: String, filename: String) = Files.exists(Paths.get(path + filename))

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

    override fun list(path: String): List<FileResource> {
        return try {
            Files.list(Paths.get(path)).map {
                FileResource(it.fileName.toString(), getContentType(it))
            }.toList()
        } catch (e: IOException) {
            listOf()
        }
    }

    override fun get(path: String, filename: String): InputStream {
        return Files.newInputStream(Paths.get(path + filename))
    }

    override fun delete(path: String, filename: String): Boolean {
        return Files.deleteIfExists(Paths.get(path + filename))
    }

    override fun createDirectories(url: String, path: String): String {
        return Files.createDirectories(Paths.get(url + path)).toFile().absolutePath
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