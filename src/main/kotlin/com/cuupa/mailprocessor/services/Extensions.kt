package com.cuupa.mailprocessor.services

object Extensions {

    fun ByteArray?.isNullOrEmpty(): Boolean {
        return this?.isEmpty() ?: true
    }
}