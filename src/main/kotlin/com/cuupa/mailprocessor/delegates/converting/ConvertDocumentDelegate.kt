package com.cuupa.mailprocessor.delegates.converting

import com.cuupa.mailprocessor.process.ProcessVariables
import com.cuupa.mailprocessor.services.Extensions.orFalse
import com.cuupa.mailprocessor.services.files.transfer.TransferProtocolFacade
import com.cuupa.mailprocessor.userconfiguration.ConverterConfiguration
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.engine.delegate.JavaDelegate

class ConvertDocumentDelegate(private val config: ConverterConfiguration) : JavaDelegate {

    override fun execute(execution: DelegateExecution?) {
        if (config.enabled.orFalse()) {
            return
        }
        val variables = ProcessVariables(execution)

        if (config.input.isNullOrEmpty() || config.output.isNullOrEmpty()) {
            return
        }

        TransferProtocolFacade.getForPath(config.input).init(config.username, config.password).use {
            it.save(config.input!!, execution?.id!!, variables.content!!)
        }
    }
}