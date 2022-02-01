package com.cuupa.mailprocessor.delegates.preprocessing.scan

import com.cuupa.mailprocessor.process.ProcessVariables
import com.cuupa.mailprocessor.services.Extensions.isNullOrEmpty
import com.cuupa.mailprocessor.services.extractors.FiletypeDetector
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.engine.delegate.JavaDelegate
import org.springframework.http.MediaType

class DetermineFiletypeDelegate : JavaDelegate {

    override fun execute(execution: DelegateExecution?) {
        val processVariables = ProcessVariables(execution)
        processVariables.contentType = determineFiletype(processVariables.content)
    }

    private fun determineFiletype(content: ByteArray?): MediaType {
        return when {
            content.isNullOrEmpty() -> MediaType.APPLICATION_OCTET_STREAM
            FiletypeDetector.isPdf(content) -> MediaType.APPLICATION_PDF
            FiletypeDetector.isTiff(content) ->  MediaType("image", "tiff")
            FiletypeDetector.isJpeg(content) -> MediaType.IMAGE_JPEG
            else -> throw RuntimeException("invalid filetype for '$content'")
        }
    }
}