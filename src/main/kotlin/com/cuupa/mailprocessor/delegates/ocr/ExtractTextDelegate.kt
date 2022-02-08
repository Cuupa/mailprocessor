package com.cuupa.mailprocessor.delegates.ocr

import com.cuupa.mailprocessor.delegates.AbstractJavaDelegate
import com.cuupa.mailprocessor.process.ProcessVariables
import com.cuupa.mailprocessor.services.files.content.FileFacade
import com.cuupa.mailprocessor.userconfiguration.WorkDirectory
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import org.camunda.bpm.engine.delegate.DelegateExecution

class ExtractTextDelegate(private val workDirectory: WorkDirectory?) : AbstractJavaDelegate() {

    override fun execute(execution: DelegateExecution?) {
        val variables = ProcessVariables(execution)
        val content = getContent(variables.id, workDirectory)
        variables.plaintext = FileFacade.content(content).getText()

        log.error("${this.javaClass.simpleName} executed")
    }

    companion object {
        private val log: Log = LogFactory.getLog(ExtractTextDelegate::class.java)
    }
}