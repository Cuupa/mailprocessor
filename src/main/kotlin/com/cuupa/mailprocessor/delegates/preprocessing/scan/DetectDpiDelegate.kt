package com.cuupa.mailprocessor.delegates.preprocessing.scan

import com.cuupa.mailprocessor.delegates.AbstractJavaDelegate
import com.cuupa.mailprocessor.process.ProcessVariables
import com.cuupa.mailprocessor.services.files.content.FileFacade
import com.cuupa.mailprocessor.services.files.transfer.TransferProtocolFacade
import com.cuupa.mailprocessor.userconfiguration.WorkDirectory
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.engine.delegate.JavaDelegate


class DetectDpiDelegate(private val workConfig: WorkDirectory) : AbstractJavaDelegate() {

    override fun execute(execution: DelegateExecution?) {
        val variables = ProcessVariables(execution)
        val content = getContent(variables.id!!, workConfig)
        variables.pageDPIs = FileFacade.content(content).getDPIPerPage()
        log.error("${this.javaClass.simpleName} executed")
    }

    companion object{
        private val log: Log = LogFactory.getLog(DetectDpiDelegate::class.java)
    }
}