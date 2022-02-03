package com.cuupa.mailprocessor.services

import com.google.zxing.BarcodeFormat


data class BarcodeResult(val pageIndex: Int, val barcode: Barcode) {

    fun isPageSeparationSheet() = barcode.text == pageSeparationCodeT || barcode.text == pageSeparationCode2
    fun isColorToggle() = barcode.barcodeFormat == BarcodeFormat.CODE_39 && barcode.text == colorToggleCode


    companion object {
        const val pageSeparationCodeT = "PATCHT"
        const val pageSeparationCode2 = "PATCH2"
        const val colorToggleCode = "PATCH4"
    }
}
