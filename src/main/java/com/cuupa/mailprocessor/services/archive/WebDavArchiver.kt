package com.cuupa.mailprocessor.services.archive

import com.github.sardine.DavResource
import com.github.sardine.Sardine
import com.github.sardine.SardineFactory
import java.io.IOException
import java.util.*

class WebDavArchiver : FileProtocol {
    private var sardine: Sardine? = null

    override fun init(username: String?, password: String?) {
        sardine = if (username.isNullOrEmpty() || password.isNullOrEmpty()) {
            SardineFactory.begin()
        } else {
            SardineFactory.begin(username, password)
        }
        sardine?.enableCompression()
    }

    override fun exists(path: String): Boolean {
        return try {
            sardine!!.exists(path)
        } catch (e: IOException) {
            false
        }
    }

    override fun save(path: String, data: ByteArray): Boolean {
        return try {
            sardine!!.put(path, data)
            true
        } catch (e: IOException) {
            false
        }
    }

    override fun createDirectory(path: String): Boolean {
        return try {
            sardine!!.createDirectory(path)
            true
        } catch (e: IOException) {
            false
        }
    }

    override fun list(path: String): List<ArchiveResource> {
        return try {
            sardine!!.list(path, 1)
                    .map { e: DavResource -> ArchiveResource(e.name, e.contentType) }
        } catch (e: IOException) {
            ArrayList()
        }
    }

    override fun close() {
        sardine?.shutdown()
    }
}