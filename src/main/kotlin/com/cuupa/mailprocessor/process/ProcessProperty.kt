package com.cuupa.mailprocessor.process

enum class ProcessProperty(val value: String) {
    USERNAME("username"),

    /**
     * Document specific fields
     */
    PATCH_SHEETS("patch_sheet"),
    HAS_PATCH_SHEET("has_patch_sheet"),
    DPI_PER_PAGE("dpi_per_page"),
    ID("id"),
    FILE_NAME("file_name"),
    PLAIN_TEXT("plain_text"),
    CONTENT_TYPE("content_type"),
    DOCUMENTS("documents"),

    /**
     * Semantic fields
     */
    TOPICS("topics"),
    SENDERS("senders"),
    METADATA("metadata"),
    HAS_SEMANTIC_RESULT("has_semantic_result"),

    /**
     * Archiving
     */
    TARGET_OK("target_ok"),
    TARGET_PATH("target_path"),

    /**
     * OCR and Converting
     */
    OCR_DONE("ocr_done"),
    NUMBER_OF_OCR_ATTEMPTS("number_of_ocr_attempts"),
    NUMBER_OF_CONVERTING_ATTEMPTS("number_of_converting_attempts")
}