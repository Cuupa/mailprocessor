package com.cuupa.mailprocessor.services.archive

import org.springframework.util.Assert
import java.util.regex.Pattern

/**
 * Determines which FileProtocol to use. Currently supports WebDAV and local storage for windows and UNIX
 */
object FileProtocolFactory {

    private val patternWindows = Pattern.compile("([A-Z]):\\\\[a-zA-Z0-9\\.\\\\]*")

    private val patternUnix = Pattern.compile("/[a-zA-Z0-9/]*")

    fun getForPath(path: String): FileProtocol? {
        Assert.hasText(path, "no path provided")
        if (isWebDav(path)) {
            return WebDavArchiver()
        } else if (isLocal(path)) {
            return LocalArchiver()
        }
        return null
    }

    private fun isLocal(path: String): Boolean {
        return patternWindows.matcher(path.replace("/", "\\"))
                .matches() || patternUnix.matcher(path).matches()
    }

    private fun isWebDav(path: String): Boolean {
        return path.startsWith("http://") || path.startsWith("https://")
    }
}