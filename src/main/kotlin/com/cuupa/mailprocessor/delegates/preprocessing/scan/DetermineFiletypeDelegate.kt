package com.cuupa.mailprocessor.delegates.preprocessing.scan

import com.cuupa.mailprocessor.delegates.AbstractJavaDelegate
import com.cuupa.mailprocessor.process.ProcessVariables
import com.cuupa.mailprocessor.services.Extensions.isNullOrEmpty
import com.cuupa.mailprocessor.services.extractors.FiletypeDetector
import com.cuupa.mailprocessor.services.files.transfer.TransferProtocolFacade
import com.cuupa.mailprocessor.userconfiguration.WorkDirectory
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.engine.delegate.JavaDelegate
import org.springframework.http.MediaType

class DetermineFiletypeDelegate(private val workConfig: WorkDirectory) : AbstractJavaDelegate() {

    override fun execute(execution: DelegateExecution?) {
        val processVariables = ProcessVariables(execution)
        val content = getContent(processVariables.id!!, workConfig)
        processVariables.contentType = determineFiletype(content)
        log.error("${this.javaClass.simpleName} executed")
    }

    private fun determineFiletype(content: ByteArray?): MediaType {
        return when {
            content.isNullOrEmpty() -> MediaType.APPLICATION_OCTET_STREAM
            FiletypeDetector.isPdf(content) -> MediaType.APPLICATION_PDF
            FiletypeDetector.isTiff(content) -> MediaType("image", "tiff")
            FiletypeDetector.isJpeg(content) -> MediaType.IMAGE_JPEG
            else -> throw RuntimeException("invalid filetype for '$content'")
        }
    }

    companion object {
        private val log: Log = LogFactory.getLog(ColorToggleDelegate::class.java)
    }
}