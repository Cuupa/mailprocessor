package com.cuupa.mailprocessor.services.files

import org.apache.commons.lang3.builder.ToStringBuilder

class FileResource internal constructor(val name: String, private val contentType: String) {
    val isFile: Boolean
        get() = directoryContentType != contentType

    val isPdf: Boolean
        get() = pdfContentType == contentType

    override fun toString(): String {
        return ToStringBuilder(this).append("name", name)
                .append("contentType", contentType)
                .append("isPdf", isPdf)
                .append("isFile", isFile)
                .toString()
    }

    companion object {
        private const val pdfContentType = "application/pdf"
        private const val directoryContentType = "httpd/unix-directory"
    }

}