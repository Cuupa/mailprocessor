package com.cuupa.mailprocessor.delegates

import com.cuupa.mailprocessor.process.ProcessInstanceHandler
import com.cuupa.mailprocessor.services.semantic.Metadata
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.engine.delegate.JavaDelegate
import java.util.regex.Pattern
import java.util.stream.Collectors

class DmnResultMapperDelegate : JavaDelegate {

    private val senderRegex = "%sender%".toRegex()

    private val regexPlaceholder = Pattern.compile("\\%[a-zA-Z]*\\%")

    override fun execute(delegateExecution: DelegateExecution) {
        val handler = ProcessInstanceHandler(delegateExecution)
        val pathToSave = getPathToSave(handler.dmnResult)
        val replacedPath = replacePlaceholder(pathToSave, handler)
        handler.setPathToSave(replacedPath).setHasReminder(getReminder(handler.dmnResult))
    }

    private fun replacePlaceholder(pathToSave: String?, handler: ProcessInstanceHandler): String? {
        var newPath = pathToSave
        if (!handler.sender.isNullOrEmpty()) {
            newPath = newPath?.replace(senderRegex, handler.sender!!)
        }
        val matcher = regexPlaceholder.matcher(pathToSave)
        while (matcher.find()) {
            val varName = matcher.group()
            val collect = handler.metadata
                    .filter { e: Metadata -> e.name == varName.replace("%", "") }
            newPath = newPath!!.replace(varName, collect.stream()
                    .map { obj: Metadata -> obj.value }
                    .collect(Collectors.joining("_")))
        }
        return newPath
    }

    private fun getPathToSave(dmn_result: Map<String, Any>): String {
        return if (dmn_result.contains("PATH_TO_SAVE")) {
            dmn_result["PATH_TO_SAVE"] as String
        } else {
            ""
        }
    }

    private fun getReminder(dmn_result: Map<String, Any>): Boolean {
        return if (dmn_result.contains("hasReminder")) {
            dmn_result["hasReminder"] as Boolean
        } else {
            false
        }
    }
}