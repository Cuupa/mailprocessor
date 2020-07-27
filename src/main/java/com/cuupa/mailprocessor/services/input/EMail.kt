package com.cuupa.mailprocessor.services.input

import java.util.*

class EMail : Document() {

    var from: String? = null
    var subject: String? = null
    var label: String? = null
    var receivedDate: Date? = null
    var attachments = mutableListOf<Attachment>()
}
