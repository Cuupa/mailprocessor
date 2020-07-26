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

    override fun exists(path: String, filename: String): Boolean {
        return try {
            sardine!!.exists(getUrl(path, filename))
        } catch (e: IOException) {
            false
        }
    }

    override fun save(path: String, filename: String, data: ByteArray): Boolean {
        return try {
            sardine!!.put(getUrl(path, filename), data)
            true
        } catch (e: IOException) {
            false
        }
    }

    override fun createDirectory(path: String): Boolean {
        return try {
            sardine!!.createDirectory(getUrl(path, ""))
            true
        } catch (e: IOException) {
            false
        }
    }

    override fun list(path: String): List<ArchiveResource> {
        return try {
            sardine!!.list(path, 1).map { e: DavResource -> ArchiveResource(e.name, e.contentType) }
        } catch (e: IOException) {
            ArrayList()
        }
    }

    override fun get(path: String, filename: String): InputStream {
        return sardine!!.get(getUrl(path, filename))
    }

    override fun delete(path: String, filename: String): Boolean {
        return try {
            sardine!!.delete(getUrl(path, filename))
            true
        } catch (e: IOException) {
            false
        }
    }

    override fun createDirectories(url: String, path: String): String {
        val pathTemp = StringBuilder("/")
        Arrays.stream(path.split("/".toRegex()).toTypedArray())
                .filter { cs: String? -> !cs.isNullOrBlank() }
                .forEach { e: String ->
                    pathTemp.append(e)
                    pathTemp.append("/")
                    val urlWithPath = url + pathTemp.toString().substring(0, pathTemp.toString().length - 1)
                    if (!exists(urlWithPath, "")) {
                        createDirectory(urlWithPath)
                    }
                }
        return url + pathTemp.toString()
    }

    private fun getUrl(path: String, name: String): String {
        val pathReplaced = path.replace(" ", "%20")
        val nameReplaced = name.replace(" ", "%20")
        val scheme = getScheme(pathReplaced)
        val host = getHost(pathReplaced)
        val port = getPort(pathReplaced.replace(scheme, "").replace("://", ""))
        val filename = pathReplaced.replace("://", "")
                .replace(":", "")
                .replace(scheme, "")
                .replace(host, "")
                .replace("$port", "") + getName(nameReplaced)
        return URI(scheme, null, host, port, filename, null, null).toURL().toString()
    }

    private fun getName(name: String): String {
        return if (name.isNullOrEmpty()) {
            ""
        } else {
            "/$name"
        }
    }

    private fun getPort(path: String): Int {
        if (!path.contains(colon)) {
            return -1
        }

        val addressArray = path.split(colonRegex)
        val port = addressArray[addressArray.size - 1]
        return if (port.contains("/")) {
            port.split("/".toRegex())[0].toInt()
        } else {
            port.toInt()
        }
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