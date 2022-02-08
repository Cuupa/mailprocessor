package com.cuupa.mailprocessor.delegates.converting

import com.cuupa.mailprocessor.delegates.AbstractJavaDelegate
import com.cuupa.mailprocessor.process.ProcessVariables
import com.cuupa.mailprocessor.services.Extensions.orFalse
import com.cuupa.mailprocessor.services.files.transfer.TransferProtocolFacade
import com.cuupa.mailprocessor.userconfiguration.ConverterConfiguration
import com.cuupa.mailprocessor.userconfiguration.WorkDirectory
import org.camunda.bpm.engine.delegate.DelegateExecution

class ConvertDocumentDelegate(
    private val config: ConverterConfiguration,
    private val workConfig: WorkDirectory
) : AbstractJavaDelegate() {

    override fun execute(execution: DelegateExecution?) {
        if (config.enabled.orFalse()) {
            return
        }
        val variables = ProcessVariables(execution)

        if (config.input.isNullOrEmpty() || config.output.isNullOrEmpty()) {
            return
        }

        val content = getContent(variables.id!!, workConfig)

        TransferProtocolFacade.getForPath(config.input)
            .init(config.username, config.password).use {
            it.save(config.input!!, execution?.id!!, content)
        }
    }
}