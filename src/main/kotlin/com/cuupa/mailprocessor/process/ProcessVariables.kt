package com.cuupa.mailprocessor.process

import com.cuupa.mailprocessor.services.files.util.DPI
import com.cuupa.mailprocessor.services.BarcodeResult
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.springframework.http.MediaType

class ProcessVariables(private val delegateExecution: DelegateExecution?) {

    var patchSheets: List<BarcodeResult>
        set(value) {
            delegateExecution?.let {
                it.setVariable(ProcessProperty.PATCH_SHEETS.value, value)
            }
        }
        get() {
            delegateExecution?.let {
                return (it.getVariable(ProcessProperty.PATCH_SHEETS.value)
                    ?: listOf<BarcodeResult>()) as List<BarcodeResult>
            }
            return listOf()
        }

    var hasPatchSheet: Boolean
        set(value) {
            delegateExecution?.let { it.variables[ProcessProperty.HAS_PATCH_SHEET.value] = value }
        }
        get() {
            delegateExecution?.let {
                return (it.getVariable(ProcessProperty.HAS_PATCH_SHEET.value) ?: false) as Boolean
            }
            return false
        }

    var content: ByteArray?
        set(value) {
            delegateExecution?.let {
                it.setVariable(ProcessProperty.FILE_CONTENT.value, value)
            }
        }
        get() {
            delegateExecution?.let {
                val content = it.getVariable(ProcessProperty.FILE_CONTENT.value)
                if (content != null && content is ByteArray) {
                    return content
                }
            }
            return null
        }

    var filename: String
        set(value) {
            delegateExecution?.let {
                it.setVariable(ProcessProperty.FILE_NAME.value, value)
            }
        }
        get() {
            delegateExecution?.let {
                return it.getVariable(ProcessProperty.FILE_NAME.value) as String
            }
            return ""
        }

    var plaintext: List<String>
        set(value) {
            delegateExecution?.let {
                it.setVariable(ProcessProperty.PLAIN_TEXT.value, value)
            }
        }
        get() {
            delegateExecution?.let {
                val content = it.getVariable(ProcessProperty.PLAIN_TEXT.value)
                if (content != null && content is List<*>) {
                    return content as List<String>
                }
            }
            return listOf()
        }

    var contentType: MediaType
        set(value) {
            delegateExecution?.let {
                it.setVariable(ProcessProperty.CONTENT_TYPE.value, value.toString())
            }
        }
        get() {
            delegateExecution?.let {
                val mediaType = it.getVariable(ProcessProperty.CONTENT_TYPE.value) as String
                val split = mediaType.split("/")
                return MediaType(split[0], split[1])
            }
            return MediaType.APPLICATION_OCTET_STREAM
        }

    var pageDPIs: List<DPI>
        set(value) {
            delegateExecution?.let {
                it.setVariable(ProcessProperty.DPI_PER_PAGE.value, value)
            }
        }
        get() {
            delegateExecution?.let {
                return it.getVariable(ProcessProperty.DPI_PER_PAGE.value) as List<DPI>
            }
            return listOf()
        }

    var hasSemanticResult: Boolean
        set(value) {
            delegateExecution?.let {
                it.setVariable(ProcessProperty.HAS_SEMANTIC_RESULT.value, value)
            }
        }
        get() {
            delegateExecution?.let {
                return it.getVariable(ProcessProperty.HAS_SEMANTIC_RESULT.value) as Boolean
            }
            return false
        }

    var topics: List<String>
        set(value) {
            delegateExecution?.let {
                it.setVariable(ProcessProperty.TOPICS.value, value)
            }
        }
        get() {
            delegateExecution?.let {
                return it.getVariable(ProcessProperty.TOPICS.value) as List<String>
            }
            return listOf()
        }

    var senders: List<String>
        set(value) {
            delegateExecution?.let {
                it.setVariable(ProcessProperty.SENDERS.value, value)
            }
        }
        get() {
            delegateExecution?.let {
                return it.getVariable(ProcessProperty.SENDERS.value) as List<String>
            }
            return listOf()
        }

    var metadatas: Map<String, List<String>>
        set(value) {
            delegateExecution?.let {
                it.setVariable(ProcessProperty.METADATA.value, value)
            }
        }
        get() {
            delegateExecution?.let {
                return it.getVariable(ProcessProperty.METADATA.value) as Map<String, List<String>>
            }
            return mapOf()
        }

    var username: String
        set(value) {
            delegateExecution?.let {
                it.setVariable(ProcessProperty.USERNAME.value, value)
            }
        }
        get() {
            delegateExecution?.let {
                return it.getVariable(ProcessProperty.USERNAME.value) as String
            }
            return ""
        }

    var targetReachable: Boolean
        set(value) {
            delegateExecution?.let { it.variables[ProcessProperty.TARGET_OK.value] = value }
        }
        get() {
            delegateExecution?.let {
                return (it.getVariable(ProcessProperty.TARGET_OK.value) ?: false) as Boolean
            }
            return false
        }

    var targetPath: String
        set(value) {
            delegateExecution?.let {
                it.setVariable(ProcessProperty.TARGET_PATH.value, value)
            }
        }
        get() {
            delegateExecution?.let {
                return it.getVariable(ProcessProperty.TARGET_PATH.value) as String
            }
            return ""
        }

    var ocrDone: Boolean
        set(value) {
            delegateExecution?.let { it.variables[ProcessProperty.OCR_DONE.value] = value }
        }
        get() {
            delegateExecution?.let {
                return (it.getVariable(ProcessProperty.OCR_DONE.value) ?: false) as Boolean
            }
            return false
        }

    var numberOfOcrAttempts: Int
        set(value) {
            delegateExecution?.let {
                it.setVariable(ProcessProperty.NUMBER_OF_OCR_ATTEMPTS.value, value)
            }
        }
        get() {
            delegateExecution?.let {
                return it.getVariable(ProcessProperty.NUMBER_OF_OCR_ATTEMPTS.value) as Int
            }
            return 0
        }

    var numberOfConvertingAttempts: Int
        set(value) {
            delegateExecution?.let {
                it.setVariable(ProcessProperty.NUMBER_OF_CONVERTING_ATTEMPTS.value, value)
            }
        }
        get() {
            delegateExecution?.let {
                return it.getVariable(ProcessProperty.NUMBER_OF_CONVERTING_ATTEMPTS.value) as Int
            }
            return 0
        }

}
