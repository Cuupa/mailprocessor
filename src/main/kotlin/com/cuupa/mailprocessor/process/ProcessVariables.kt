package com.cuupa.mailprocessor.process

import com.cuupa.mailprocessor.delegates.preprocessing.scan.FileType
import com.cuupa.mailprocessor.services.BarcodeResult
import org.camunda.bpm.engine.delegate.DelegateExecution

class ProcessVariables(private val delegateExecution: DelegateExecution?) {

    var patchSheets: List<BarcodeResult>
        set(value) {
            delegateExecution?.let { it.variables[ProcessProperty.PATCH_SHEETS.name] = value }
        }
        get() {
            delegateExecution?.let {
                return (((it.variables[ProcessProperty.HAS_PATCH_SHEET.name] ?: listOf<BarcodeResult>()) as List<BarcodeResult>))
            }
            return listOf()
        }

    var hasPatchSheet: Boolean
        set(value) {
            delegateExecution?.let { it.variables[ProcessProperty.HAS_PATCH_SHEET.name] = value }
        }
        get() {
            delegateExecution?.let {
                return (it.variables[ProcessProperty.HAS_PATCH_SHEET.name] ?: false) as Boolean
            }
            return false
        }

    var content: ByteArray?
        set(value) {
            delegateExecution?.let { it.variables[ProcessProperty.FILE_CONTENT .name] = value }
        }
        get() {
            delegateExecution?.let {
                val content = it.variables[ProcessProperty.FILE_CONTENT.name]
                if (content != null && content is ByteArray) {
                    return content
                }
            }
            return null
        }

    var filetype: FileType
        set(value) {
            delegateExecution?.let { it.variables[ProcessProperty.FILE_TYPE.name] = value }
        }
        get() {
            delegateExecution?.let {
                return FileType.valueOf(it.variables[ProcessProperty.FILE_TYPE.name] as String)
            }
            return FileType.NONE
        }

}
