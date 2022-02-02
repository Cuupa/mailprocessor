package com.cuupa.mailprocessor.delegates.ocr

import com.cuupa.mailprocessor.process.ProcessVariables
import com.cuupa.mailprocessor.services.Extensions.orFalse
import com.cuupa.mailprocessor.services.files.content.FileFacade
import com.cuupa.mailprocessor.services.files.transfer.TransferProtocolFacade
import com.cuupa.mailprocessor.userconfiguration.OcrConfiguration
import org.apache.commons.io.IOUtils
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.engine.delegate.JavaDelegate

class CheckOCRDelegate(private val config: OcrConfiguration) : JavaDelegate {

    override fun execute(execution: DelegateExecution?) {
        if (config.enabeld.orFalse()) {
            return
        }

        if (config.output.isNullOrEmpty()) {
            return
        }

        val variables = ProcessVariables(execution)

        TransferProtocolFacade.getForPath(config.output).init(config.username, config.password).use {
            if(it.exists(config.output, variables.ocrId)) {
                variables.content = IOUtils.toByteArray(it.get(config.output!!, variables.ocrId!!))
                variables.plaintext = FileFacade.content(variables.content).getText()
                variables.ocrDone = true
            }
        }
    }
}
