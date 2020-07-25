package com.cuupa.mailprocessor.services.archive

interface FileProtocol : AutoCloseable {

    fun init(username: String?, password: String?)

    fun exists(path: String): Boolean

    fun save(path: String, data: ByteArray): Boolean

    fun createDirectory(path: String): Boolean

    fun list(path: String): List<ArchiveResource>
}