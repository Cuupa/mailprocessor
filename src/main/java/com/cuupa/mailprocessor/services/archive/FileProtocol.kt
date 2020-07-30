package com.cuupa.mailprocessor.services.archive

import java.io.InputStream

interface FileProtocol : AutoCloseable {

    fun init(username: String?, password: String?)

    fun exists(path: String, filename: String): Boolean

    fun save(path: String, filename: String, data: ByteArray): Boolean

    fun createDirectory(path: String): Boolean

    fun list(path: String): List<FileResource>

    fun get(path: String, filename: String): InputStream

    fun delete(path: String, filename: String): Boolean

    fun createDirectories(url: String, path: String): String
}