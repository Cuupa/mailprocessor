package com.cuupa.mailprocessor.process

import com.cuupa.mailprocessor.services.files.util.DPI
import com.cuupa.mailprocessor.services.BarcodeResult
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.springframework.http.MediaType

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

    var filename: String
        set(value) {
            delegateExecution?.let {
                it.setVariable(ProcessProperty.FILE_NAME.name, value)
            }
        }
        get() {
            delegateExecution?.let {
                return it.getVariable(ProcessProperty.FILE_NAME.name) as String
            }
            return ""
        }

    var plaintext: List<String>
        set(value) {
            delegateExecution?.let {
                it.setVariable(ProcessProperty.PLAIN_TEXT.name, value)
            }
        }
        get() {
            delegateExecution?.let {
                val content = it.getVariable(ProcessProperty.PLAIN_TEXT.name)
                if (content != null && content is List<*>) {
                    return content as List<String>
                }
            }
            return listOf()
        }

    var contentType: MediaType
        set(value) {
            delegateExecution?.let {
                it.setVariable(ProcessProperty.CONTENT_TYPE.name, value.toString())
            }
        }
        get() {
            delegateExecution?.let {
                val mediaType = it.getVariable(ProcessProperty.CONTENT_TYPE.name) as String
                val split = mediaType.split("/")
                return MediaType(split[0], split[1])
            }
            return MediaType.APPLICATION_OCTET_STREAM
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

    var hasSemanticResult: Boolean
        set(value) {
            delegateExecution?.let {
                it.setVariable(ProcessProperty.HAS_SEMANTIC_RESULT.name, value)
            }
        }
        get() {
            delegateExecution?.let {
                return it.getVariable(ProcessProperty.HAS_SEMANTIC_RESULT.name) as Boolean
            }
            return false
        }

    var topics: List<String>
        set(value) {
            delegateExecution?.let {
                it.setVariable(ProcessProperty.TOPICS.name, value)
            }
        }
        get() {
            delegateExecution?.let {
                return it.getVariable(ProcessProperty.TOPICS.name) as List<String>
            }
            return listOf()
        }

    var senders: List<String>
        set(value) {
            delegateExecution?.let {
                it.setVariable(ProcessProperty.SENDERS.name, value)
            }
        }
        get() {
            delegateExecution?.let {
                return it.getVariable(ProcessProperty.SENDERS.name) as List<String>
            }
            return listOf()
        }

    var metadatas: Map<String, List<String>>
        set(value) {
            delegateExecution?.let {
                it.setVariable(ProcessProperty.METADATA.name, value)
            }
        }
        get() {
            delegateExecution?.let {
                return it.getVariable(ProcessProperty.METADATA.name) as Map<String, List<String>>
            }
            return mapOf()
        }

    var username: String
        set(value) {
            delegateExecution?.let {
                it.setVariable(ProcessProperty.USERNAME.name, value)
            }
        }
        get() {
            delegateExecution?.let {
                return it.getVariable(ProcessProperty.USERNAME.name) as String
            }
            return ""
        }

    var targetReachable: Boolean
        set(value) {
            delegateExecution?.let { it.variables[ProcessProperty.TARGET_OK.name] = value }
        }
        get() {
            delegateExecution?.let {
                return (it.getVariable(ProcessProperty.TARGET_OK.name) ?: false) as Boolean
            }
            return false
        }

    var targetPath: String
        set(value) {
            delegateExecution?.let {
                it.setVariable(ProcessProperty.TARGET_PATH.name, value)
            }
        }
        get() {
            delegateExecution?.let {
                return it.getVariable(ProcessProperty.TARGET_PATH.name) as String
            }
            return ""
        }

    var ocrDone: Boolean
        set(value) {
            delegateExecution?.let { it.variables[ProcessProperty.OCR_DONE.name] = value }
        }
        get() {
            delegateExecution?.let {
                return (it.getVariable(ProcessProperty.OCR_DONE.name) ?: false) as Boolean
            }
            return false
        }

    var ocrId: String?
        set(value) {
            delegateExecution?.let {
                it.setVariable(ProcessProperty.TARGET_PATH.name, value)
            }
        }
        get() {
            delegateExecution?.let {
                return it.getVariable(ProcessProperty.TARGET_PATH.name) as String
            }
            return null
        }

    var numberOfOcrAttempts: Int
        set(value) {
            delegateExecution?.let {
                it.setVariable(ProcessProperty.NUMBER_OF_OCR_ATTEMPTS.name, value)
            }
        }
        get() {
            delegateExecution?.let {
                return it.getVariable(ProcessProperty.NUMBER_OF_OCR_ATTEMPTS.name) as Int
            }
            return 0
        }

}
