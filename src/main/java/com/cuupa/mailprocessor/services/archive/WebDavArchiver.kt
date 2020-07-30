package com.cuupa.mailprocessor.services.archive

import com.cuupa.mailprocessor.utli.UrlStringUnifier
import com.github.sardine.DavResource
import com.github.sardine.Sardine
import com.github.sardine.SardineFactory
import java.io.IOException
import java.io.InputStream
import java.net.URI
import java.util.*

class WebDavArchiver : FileProtocol {

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
            sardine!!.createDirectory(getUrl(path, emptyString))
            true
        } catch (e: IOException) {
            false
        }
    }

    override fun list(path: String): List<ArchiveResource> {
        return try {
            sardine!!.list(getUrl(path, emptyString), 1)
                    .map { e: DavResource -> ArchiveResource(e.name, e.contentType) }
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
        val pathTemp = StringBuilder(seperator)
        Arrays.stream(path.split(seperatorRegex).toTypedArray())
                .filter { cs: String? -> !cs.isNullOrBlank() }
                .forEach { e: String ->
                    pathTemp.append(e)
                    pathTemp.append(seperator)
                    val urlWithPath = url + pathTemp.toString().substring(0, pathTemp.toString().length - 1)
                    if (!exists(urlWithPath, emptyString)) {
                        createDirectory(urlWithPath)
                    }
                }
        return url + pathTemp.toString()
    }

    private fun getUrl(path: String, name: String): String {
        val pathReplaced = unifier.unify(path)
        val nameReplaced = unifier.unify(name)
        val scheme = getScheme(pathReplaced)
        val host = getHost(pathReplaced)
        val port = getPort(pathReplaced.replace(scheme, emptyString).replace(schemaSeperator, emptyString))
        val filename = getFilename(pathReplaced, scheme, host, port, nameReplaced)
        return URI(scheme, null, host, port, filename, null, null).toURL().toString()
    }

    private fun getFilename(filename: String, scheme: String, host: String, port: Int, nameReplaced: String): String {
        return filename.replace(schemaSeperator, emptyString)
                .replace(colon, emptyString)
                .replace(scheme, emptyString)
                .replace(host, emptyString)
                .replace("$port", emptyString) + getName(nameReplaced)
    }

    private fun getName(name: String): String {
        return if (name.isNullOrEmpty()) {
            emptyString
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
        return if (port.contains(seperator)) {
            port.split(seperatorRegex)[0].toInt()
        } else {
            port.toInt()
        }
    }

    private fun getHost(path: String): String {
        val addressWithoutScheme: String = path.split(schemaSeperatorRegex)[1]
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

    companion object {
        private val unifier = UrlStringUnifier()
        private var sardine: Sardine? = null

        private val emptyString = ""
        private val seperator = "/"
        private val seperatorRegex = seperator.toRegex()
        private val colon = ":"
        private val colonRegex = colon.toRegex()
        private val schemaSeperator = "://"
        private val schemaSeperatorRegex = schemaSeperator.toRegex()
    }
}