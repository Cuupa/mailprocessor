package com.cuupa.mailprocessor.services.files.content

import com.cuupa.mailprocessor.services.extractors.FiletypeDetector

object FileFacade {

    fun content(content: ByteArray?): FileContent {

        if (content == null) {
            return Invalid()
        }

        return when {
            FiletypeDetector.isPdf(content) -> Pdf(content)
            FiletypeDetector.isJpeg(content) -> Jpg(content)
            FiletypeDetector.isTiff(content) -> Tiff(content)
            else -> Invalid()
        }
    }
}