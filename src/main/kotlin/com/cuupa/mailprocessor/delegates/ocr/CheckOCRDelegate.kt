package com.cuupa.mailprocessor.delegates.ocr

import com.cuupa.mailprocessor.delegates.AbstractJavaDelegate
import com.cuupa.mailprocessor.process.ProcessVariables
import com.cuupa.mailprocessor.services.Extensions.orFalse
import com.cuupa.mailprocessor.services.files.content.FileFacade
import com.cuupa.mailprocessor.services.files.transfer.TransferProtocolFacade
import com.cuupa.mailprocessor.userconfiguration.OcrConfiguration
import com.cuupa.mailprocessor.userconfiguration.WorkDirectory
import org.apache.commons.io.IOUtils
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.engine.delegate.JavaDelegate

class CheckOCRDelegate(
    private val config: OcrConfiguration,
    private val workDirectory: WorkDirectory
) : AbstractJavaDelegate() {

    override fun execute(execution: DelegateExecution?) {
        if (config.enabled.orFalse()) {
            return
        }

        if (config.output.isNullOrEmpty()) {
            return
        }

        val variables = ProcessVariables(execution)

        TransferProtocolFacade.getForPath(config.output).init(config.username, config.password).use {
            if (it.exists(config.output, execution?.id)) {
                val ocrDocument = IOUtils.toByteArray(it.get(config.output!!, execution?.id!!))
                writeContent(variables.id!!, ocrDocument, workDirectory)
                variables.plaintext = FileFacade.content(ocrDocument).getText()
                variables.ocrDone = true
            }
        }
        log.error("${this.javaClass.simpleName} executed")
    }

    companion object {
        private val log: Log = LogFactory.getLog(CheckOCRDelegate::class.java)
    }
}
