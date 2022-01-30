package com.cuupa.mailprocessor.delegates.preprocessing.scan

import com.cuupa.mailprocessor.process.ProcessVariables
import com.cuupa.mailprocessor.services.Extensions.isNullOrEmpty
import com.cuupa.mailprocessor.services.extractors.FiletypeDetector
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.engine.delegate.JavaDelegate

class FiletypeDetermineDelegate : JavaDelegate {

    override fun execute(execution: DelegateExecution?) {
        val processVariables = ProcessVariables(execution)
        processVariables.filetype = determineFiletype(processVariables.content)
    }

    private fun determineFiletype(content: ByteArray?): FileType {

        return when {
            content.isNullOrEmpty() -> FileType.NONE
            FiletypeDetector.isPdf(content) -> FileType.PDF
            FiletypeDetector.isTiff(content) -> FileType.TIFF
            FiletypeDetector.isJpeg(content) -> FileType.JPEG
            else -> throw RuntimeException("invalid filetype for '$content'")
        }
    }
}