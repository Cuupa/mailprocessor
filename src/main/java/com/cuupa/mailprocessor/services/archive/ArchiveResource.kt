package com.cuupa.mailprocessor.services.archive

import org.apache.commons.lang3.builder.ToStringBuilder

class ArchiveResource internal constructor(val name: String, private val contentType: String) {
    val isPdf: Boolean
        get() = pdfContentType == contentType

    override fun toString(): String {
        return ToStringBuilder(this).append("name", name)
                .append("contentType", contentType)
                .append("isPdf", isPdf)
                .toString()
    }

    companion object {
        private const val pdfContentType = "application/pdf"
    }

}