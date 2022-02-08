package com.cuupa.mailprocessor.delegates.archive

import com.cuupa.mailprocessor.process.ProcessVariables
import com.cuupa.mailprocessor.services.files.transfer.TransferProtocolFacade
import com.cuupa.mailprocessor.userconfiguration.OutputConfiguration
import com.cuupa.mailprocessor.userconfiguration.UserConfiguration
import com.cuupa.mailprocessor.userconfiguration.WorkDirectory
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.engine.delegate.JavaDelegate

class ArchiveDocumentDelegate(
    private val userConfigs: List<UserConfiguration>?,
    private val workConfig: WorkDirectory
) : JavaDelegate {

    override fun execute(execution: DelegateExecution?) {
        val variables = ProcessVariables(execution)
        val userConfig = userConfigs?.find { it.username == variables.username }

        userConfig?.let {
            it.output?.let { out ->
                val content = getContentFromWorkDir(variables)
                saveContent(out, variables, content)
            }
        }
    }

    private fun saveContent(
        out: OutputConfiguration,
        variables: ProcessVariables,
        content: ByteArray
    ) {
        TransferProtocolFacade.getForPath(out.path).init(out.username, out.password).use { file ->
            file.save(out.path!!, variables.filename, content)
        }
    }

    private fun getContentFromWorkDir(
        variables: ProcessVariables,
    ): ByteArray {
        TransferProtocolFacade.getForPath(variables.id)
            .init(workConfig.username, workConfig.password).use { file ->
                return file.get(workConfig.path!!, variables.id!!).readAllBytes()
            }
    }
}