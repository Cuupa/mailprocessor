package com.cuupa.mailprocessor.services.archive

import com.github.sardine.DavResource
import com.github.sardine.Sardine
import com.github.sardine.SardineFactory
import java.io.IOException
import java.io.InputStream
import java.net.URI
import java.util.*

class WebDavArchiver : FileProtocol {

    private var sardine: Sardine? = null

    private val colonRegex = ":".toRegex()

    private val colon = ":"

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

    override fun get(path: String, name: String): InputStream {
        return sardine!!.get(getUrl(path, name))
    }

    private fun getUrl(path: String, name: String): String {
        return URI(getScheme(path), null, getHost(path), getPort(getHost(path)), "/$name", null, null).toURL()
                .toString()
    }

    private fun getPort(path: String): Int {
        if (path.contains(colon)) {

            val addressArray = path.split(colonRegex)
            val port = addressArray[addressArray.size - 1]
            return port.toInt()
        }
        return -1
    }

    private fun getHost(path: String): String {
        val addressWithoutScheme: String = path.split("://".toRegex())[1]
        return if (addressWithoutScheme.contains(colon)) {
            addressWithoutScheme.split(colonRegex).toTypedArray()[0]
        } else addressWithoutScheme
    }

    private fun getScheme(path: String): String {
        return path.split(colonRegex)[0]
    }

    override fun close() {
        sardine?.shutdown()
    }
}