package com.cuupa.mailprocessor.services.input

import java.time.LocalDateTime

class EMail : Document() {

    var from: String? = null
    var subject: String? = null
    var label: String? = null
    var receivedDate: LocalDateTime? = null
    var attachments = mutableListOf<Attachment>()
}
