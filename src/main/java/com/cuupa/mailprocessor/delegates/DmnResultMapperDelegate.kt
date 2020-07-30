package com.cuupa.mailprocessor.delegates

import com.cuupa.mailprocessor.process.ProcessInstanceHandler
import com.cuupa.mailprocessor.process.ProcessProperty
import com.cuupa.mailprocessor.services.semantic.Metadata
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.engine.delegate.JavaDelegate
import java.util.regex.Pattern

class DmnResultMapperDelegate : JavaDelegate {

    override fun execute(delegateExecution: DelegateExecution) {
        val handler = ProcessInstanceHandler(delegateExecution)
        val pathToSave = getPathToSave(handler.dmnResult)
        val replacedPath = replacePlaceholder(pathToSave, handler)
        handler.pathToSave = replacedPath
        handler.hasReminder = getReminder(handler.dmnResult)
    }

    private fun replacePlaceholder(pathToSave: String, handler: ProcessInstanceHandler): String {
        var newPath = pathToSave
        if (!handler.sender.isNullOrEmpty()) {
            newPath = newPath.replace(senderRegex, handler.sender!!)
        }
        val matcher = regexPlaceholder.matcher(newPath)
        while (matcher.find()) {
            val varName = matcher.group()
            val collect = handler.metadata.filter { e: Metadata -> e.name == varName.replace("%", "") }
            newPath = newPath.replace(varName, collect.joinToString("_", "", "") { it.value })
        }
        return newPath
    }

    private fun getPathToSave(dmn_result: Map<String, Any>): String {
        return if (dmn_result.contains(ProcessProperty.PATH_TO_SAVE.name)) {
            dmn_result[ProcessProperty.PATH_TO_SAVE.name] as String
        } else {
            ""
        }
    }

    private fun getReminder(dmn_result: Map<String, Any>): Boolean {
        return if (dmn_result.contains(ProcessProperty.HAS_REMINDER.name)) {
            dmn_result[ProcessProperty.HAS_REMINDER.name] as Boolean
        } else {
            false
        }
    }

    companion object {
        private val senderRegex = "%sender%".toRegex()
        private val regexPlaceholder = Pattern.compile("\\%[a-zA-Z]*\\%")
    }
}