package com.cuupa.mailprocessor.delegates.ocr

import com.cuupa.mailprocessor.delegates.AbstractJavaDelegate
import com.cuupa.mailprocessor.process.ProcessVariables
import com.cuupa.mailprocessor.services.Extensions.orFalse
import com.cuupa.mailprocessor.services.files.transfer.TransferProtocolFacade
import com.cuupa.mailprocessor.userconfiguration.OcrConfiguration
import com.cuupa.mailprocessor.userconfiguration.WorkDirectory
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.engine.delegate.JavaDelegate

class CallOCRDelegate(
    private val config: OcrConfiguration,
    private val workDirectory: WorkDirectory
) : AbstractJavaDelegate() {

    override fun execute(execution: DelegateExecution?) {
        if (config.enabled.orFalse()) {
            return
        }
        val variables = ProcessVariables(execution)

        if (config.input.isNullOrEmpty() || config.output.isNullOrEmpty()) {
            return
        }

        val content = getContent(variables.id!!, workDirectory)

        TransferProtocolFacade.getForPath(config.input).init(config.username, config.password).use {
            it.save(config.input!!, execution?.id!!, content)
        }

        log.error("${this.javaClass.simpleName} executed")
    }

    companion object {
        private val log: Log = LogFactory.getLog(CallOCRDelegate::class.java)
    }
}
