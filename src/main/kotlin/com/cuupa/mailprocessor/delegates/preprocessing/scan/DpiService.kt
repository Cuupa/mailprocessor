package com.cuupa.mailprocessor.delegates.preprocessing.scan

import org.apache.pdfbox.contentstream.PDFStreamEngine
import org.apache.pdfbox.contentstream.operator.Operator
import org.apache.pdfbox.cos.COSBase
import org.apache.pdfbox.cos.COSName
import org.apache.pdfbox.pdmodel.PDPage
import org.apache.pdfbox.pdmodel.graphics.form.PDFormXObject
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject
import org.apache.pdfbox.util.Matrix

class DpiService(val page: PDPage) : PDFStreamEngine() {

    private lateinit var dpi: DPI

    fun determineDpi(): DPI {
        processPage(page)
        return dpi
    }

    override fun processOperator(operator: Operator?, operands: MutableList<COSBase>?) {
        val operation = operator!!.name
        if ("Do" == operation) {
            val objectName = operands!![0] as COSName
            val xobject = resources.getXObject(objectName)
            if (xobject is PDImageXObject) {
                val ctmNew: Matrix = graphicsState.currentTransformationMatrix
                dpi = DPI(
                    xobject.width * 72 / ctmNew.scalingFactorX,
                    xobject.height * 72 / ctmNew.scalingFactorY
                )


            } else if (xobject is PDFormXObject) {
                showForm(xobject)
            }
        } else {
            super.processOperator(operator, operands)
        }
    }
}