package com.cuupa.mailprocessor.services.archive

import com.github.sardine.DavResource
import com.github.sardine.Sardine
import com.github.sardine.SardineFactory
import org.apache.juli.logging.LogFactory
import java.io.IOException
import java.io.InputStream
import java.net.URI
import java.util.*

class WebDav : File {

    private var sardine: Sardine? = null

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

    /*
     * This is a utility function which prevents the following code from creating directories which already exists
     * On a Synology WebDAV, there's the possibility of a HTTP/403 or HTTP/404 if the directory already exists, but
     * the call for "exists" fails
     */
    private fun _exists(path: String, filename: String): Boolean {
        return try {
            sardine!!.exists(getUrl(path, filename))
        } catch (e: IOException) {
            true
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
            sardine!!.createDirectory(getUrl(path.replace(" ", "%20"), emptyString))
            true
        } catch (e: IOException) {
            false
        }
    }

    override fun list(path: String): List<FileResource> {
        return try {
            sardine!!.list(getUrl(path, emptyString), 1).map { e: DavResource -> FileResource(e.name, e.contentType) }
        } catch (e: IOException) {
            ArrayList()
        }
    }

    override fun get(path: String, filename: String): InputStream {
        try {
            return sardine!!.get(getUrl(path, filename))
        } catch (exception: Exception) {
            log.error("Failed to retrieve InputStream for $path/$filename", exception)
            throw exception
        }
    }

    override fun delete(path: String, filename: String): Boolean {
        return try {
            sardine!!.delete(getUrl(path,filename))
            true
        } catch (e: IOException) {
            false
        }
    }

    override fun createDirectories(url: String, path: String): String {
        val pathTemp = StringBuilder(separator)
        var urlWithPath = ""
        Arrays.stream(path.split(separatorRegex).toTypedArray())
                .filter(nonEmptyEntries())
                .map { it.replace(" ", "%20") }
                .forEach {
                    pathTemp.append(it)
                    pathTemp.append(separator)
                    urlWithPath = url + pathTemp.toString().substring(0, pathTemp.toString().length - 1)
                    if (!_exists(urlWithPath, emptyString)) {
                        createDirectory(urlWithPath)
                    }
                }
        return urlWithPath
    }

    private fun nonEmptyEntries() = { cs: String? -> !cs.isNullOrBlank() }

    private fun getUrl(path: String, name: String): String {
        val scheme = getScheme(path)
        val host = getHost(path)
        val port = getPort(path.replace(scheme, emptyString).replace(schemaSeparator, emptyString))
        val filename = getFilename(path, scheme, host, port, name)
        return URI(scheme, null, host, port, filename, null, null).toURL().toString()
    }

    private fun getFilename(filename: String, scheme: String, host: String, port: Int, nameReplaced: String): String {
        return filename.replace(schemaSeparator, emptyString)
                .replace(colon, emptyString)
                .replace(scheme, emptyString)
                .replace(host, emptyString)
                .replace("$port", emptyString) + getName(nameReplaced)
    }

    private fun getName(name: String): String {
        return if (name.isEmpty()) {
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
        return if (port.contains(separator)) {
            port.split(separatorRegex)[0].toInt()
        } else {
            port.toInt()
        }
    }

    private fun getHost(path: String): String {
        val addressWithoutScheme: String = path.split(schemaSeparatorRegex)[1]
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
        private val log = LogFactory.getLog(WebDav::class.java)
        private const val emptyString = ""
        private const val separator = "/"
        private val separatorRegex = separator.toRegex()
        private const val colon = ":"
        private val colonRegex = colon.toRegex()
        private const val schemaSeparator = "://"
        private val schemaSeparatorRegex = schemaSeparator.toRegex()
    }
}