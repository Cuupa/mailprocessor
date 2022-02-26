package com.cuupa.mailprocessor.delegates.preprocessing.scan

import com.cuupa.mailprocessor.delegates.AbstractJavaDelegate
import com.cuupa.mailprocessor.process.ProcessVariables
import com.cuupa.mailprocessor.services.files.content.FileFacade
import com.cuupa.mailprocessor.userconfiguration.WorkDirectory
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import org.camunda.bpm.engine.delegate.DelegateExecution

class DocumentSeparationDelegate(private val workConfig: WorkDirectory
) : AbstractJavaDelegate() {

    override fun execute(execution: DelegateExecution?) {
        val variables = ProcessVariables(execution)

        if (!variables.patchSheets.isNullOrEmpty()) {
            log.error("No patch sheets found, although ${DetectPatchSheetDelegate::class.java} has detected some")
        }

        val pageSeparationSheets = variables.patchSheets
            .filter { it.isPageSeparationSheet() }
            .map { it.pageIndex }

        val content = getContent(variables.id!!, workConfig)
        val file = FileFacade.content(content).handleFileSeparationPatchSheet(pageSeparationSheets)



        //variables.documents = file
        log.error("${this.javaClass.simpleName} executed")
    }

    companion object {
        val log: Log = LogFactory.getLog(DocumentSeparationDelegate::class.java)
    }
}