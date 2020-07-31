package com.cuupa.mailprocessor.process

enum class ErrorProperties(val message: String) {

    FILE_ALREADY_EXISTS("The file %s already exist."), FILE_FAILED_TO_SAVE("Failed to save file %s")
}
