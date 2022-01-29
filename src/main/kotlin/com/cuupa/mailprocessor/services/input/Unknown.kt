package com.cuupa.mailprocessor.services.input

class Unknown(content: ByteArray?) : Document(content) {
    constructor() : this(null)
}
