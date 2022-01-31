package com.cuupa.mailprocessor.process

import com.cuupa.mailprocessor.services.files.util.DPI
import com.cuupa.mailprocessor.services.files.util.FileType
import com.cuupa.mailprocessor.services.BarcodeResult
import org.camunda.bpm.engine.delegate.DelegateExecution

class ProcessVariables(private val delegateExecution: DelegateExecution?) {

    var patchSheets: List<BarcodeResult>
        set(value) {
            delegateExecution?.let {
                it.setVariable(ProcessProperty.PATCH_SHEETS.name, value)
            }
        }
        get() {
            delegateExecution?.let {
                return (it.getVariable(ProcessProperty.PATCH_SHEETS.name)
                    ?: listOf<BarcodeResult>()) as List<BarcodeResult>
            }
            return listOf()
        }

    var hasPatchSheet: Boolean
        set(value) {
            delegateExecution?.let { it.variables[ProcessProperty.HAS_PATCH_SHEET.name] = value }
        }
        get() {
            delegateExecution?.let {
                return (it.getVariable(ProcessProperty.HAS_PATCH_SHEET.name) ?: false) as Boolean
            }
            return false
        }

    var content: ByteArray?
        set(value) {
            delegateExecution?.let {
                it.setVariable(ProcessProperty.FILE_CONTENT.name, value)
            }
        }
        get() {
            delegateExecution?.let {
                val content = it.getVariable(ProcessProperty.FILE_CONTENT.name)
                if (content != null && content is ByteArray) {
                    return content
                }
            }
            return null
        }

    var filetype: FileType
        set(value) {
            delegateExecution?.let {
                it.setVariable(ProcessProperty.FILE_TYPE.name, value)
            }
        }
        get() {
            delegateExecution?.let {
                return FileType.valueOf(it.getVariable(ProcessProperty.FILE_TYPE.name) as String)
            }
            return FileType.NONE
        }

    var pageDPIs: List<DPI>
        set(value) {
            delegateExecution?.let {
                it.setVariable(ProcessProperty.DPI_PER_PAGE.name, value)
            }
        }
        get() {
            delegateExecution?.let {
                return it.getVariable(ProcessProperty.DPI_PER_PAGE.name) as List<DPI>
            }
            return listOf()
        }

}
