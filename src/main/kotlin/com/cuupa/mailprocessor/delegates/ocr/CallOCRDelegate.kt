package com.cuupa.mailprocessor.delegates.ocr

import com.cuupa.mailprocessor.process.ProcessVariables
import com.cuupa.mailprocessor.services.Extensions.orFalse
import com.cuupa.mailprocessor.services.files.transfer.TransferProtocolFacade
import com.cuupa.mailprocessor.userconfiguration.OcrConfiguration
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.engine.delegate.JavaDelegate

class CallOCRDelegate(private val config: OcrConfiguration) : JavaDelegate {

    override fun execute(execution: DelegateExecution?) {
        if (config.enabeld.orFalse()) {
            return
        }

        if (config.input.isNullOrEmpty() || config.output.isNullOrEmpty()) {
            return
        }

        val variables = ProcessVariables(execution)

        TransferProtocolFacade.getForPath(config.input).init(config.username, config.password).use {
            it.save(config.input!!, variables.filename, variables.content!!)
        }
    }
}
