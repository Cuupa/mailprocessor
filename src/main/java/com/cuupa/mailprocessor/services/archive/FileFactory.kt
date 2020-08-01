package com.cuupa.mailprocessor.services.archive

import java.util.regex.Pattern

/**
 * Determines which FileProtocol to use. Currently supports WebDAV and local storage for windows and UNIX
 */
object FileFactory {

    private val patternWindows = Pattern.compile("([A-Z]):\\\\[a-zA-Z0-9\\.\\\\]*")

    private val patternUnix = Pattern.compile("/[a-zA-Z0-9/]*")

    fun getForPath(path: String) = when {
        isWebDav(path) -> WebDav()
        isLocal(path) -> Local()
        else -> Invalid()
    }

    private fun isLocal(path: String) = patternWindows.matcher(path.replace("/", "\\"))
        .matches() || patternUnix.matcher(path).matches()

    private fun isWebDav(path: String) = path.startsWith("http://") || path.startsWith("https://")
}