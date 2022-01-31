package com.cuupa.mailprocessor.services

import com.google.zxing.BarcodeFormat

data class Barcode(val barcodeFormat: BarcodeFormat, val text: String)
