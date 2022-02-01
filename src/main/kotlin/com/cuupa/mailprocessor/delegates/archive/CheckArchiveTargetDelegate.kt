package com.cuupa.mailprocessor.delegates.archive

import com.cuupa.mailprocessor.process.ProcessVariables
import com.cuupa.mailprocessor.services.files.transfer.TransferProtocolFacade
import com.cuupa.mailprocessor.userconfiguration.UserConfiguration
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.engine.delegate.JavaDelegate

class CheckArchiveTargetDelegate(private val config: List<UserConfiguration>?) : JavaDelegate {

    override fun execute(execution: DelegateExecution?) {
        val variables = ProcessVariables(execution)
        val userConfig = config?.find { it.username == variables.username }
        userConfig?.let {
            it.output?.let { out ->
                TransferProtocolFacade.getForPath(out.path).init(out.username, out.password).use {file ->
                    variables.targetReachable = file.exists(out.path, "")
                }
            }
        }
    }
}
