package com.cuupa.mailprocessor.userconfiguration

import com.fasterxml.jackson.annotation.JsonProperty

class Configuration {

    @JsonProperty("converter")
    var converter: ConverterConfiguration? = null

    @JsonProperty("classificator")
    var classificator: ClassificatorConfiguration? = null

    @JsonProperty("OCR")
    var ocr: OcrConfiguration? = null

    @JsonProperty("decisiontables")
    var decisiontables: DecisionConfiguration? = null

    @JsonProperty("user")
    var entries: List<UserConfiguration>? = null
}