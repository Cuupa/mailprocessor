package com.cuupa.mailprocessor.services.files.transfer

import java.io.InputStream

class Invalid : File {

    override fun createDirectories(url: String, path: String): String {
        TODO("Invalid file")
    }

    override fun init(username: String?, password: String?): File {
        return this
    }

    override fun exists(path: String?, filename: String?): Boolean {
        TODO("Not yet implemented")
    }

    override fun save(path: String, filename: String, data: ByteArray): Boolean {
        TODO("Invalid file")
    }

    override fun createDirectory(path: String): Boolean {
        TODO("Invalid file")
    }

    override fun list(path: String): List<FileResource> {
        TODO("Invalid file")
    }

    override fun get(path: String, filename: String): InputStream {
        TODO("Invalid file")
    }

    override fun delete(path: String, filename: String): Boolean {
        TODO("Invalid file")
    }

    override fun close() {
        TODO("Not yet implemented")
    }
}
