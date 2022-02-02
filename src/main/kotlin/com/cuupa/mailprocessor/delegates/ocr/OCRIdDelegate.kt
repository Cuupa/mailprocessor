package com.cuupa.mailprocessor.delegates.ocr

import com.cuupa.mailprocessor.process.ProcessVariables
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.engine.delegate.JavaDelegate
import java.util.*

/**
 * @author Simon Thiel (https://github.com/cuupa)
 */
class OCRIdDelegate: JavaDelegate {

    override fun execute(execution: DelegateExecution?) {
        val variables = ProcessVariables(execution)
        variables.ocrId = UUID.randomUUID().toString()
    }
}