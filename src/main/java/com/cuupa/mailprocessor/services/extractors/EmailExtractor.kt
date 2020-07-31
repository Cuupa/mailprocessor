package com.cuupa.mailprocessor.services.extractors

import org.apache.tika.Tika
import org.apache.tika.metadata.Metadata
import java.io.ByteArrayInputStream

class EmailExtractor : TextExtractor {

    override fun extract(content: ByteArray): List<String> {
        val metadata = Metadata()
        val fileContent = Tika().parseToString(ByteArrayInputStream(content), metadata)
        val metadataValue = concatMailMetadata(metadata)
        return listOf(metadataValue, fileContent)
    }

    private fun concatMailMetadata(metadata: Metadata): String {
        return "subject: ${metadata.get("subject")}\ndc:creator: ${metadata.get("dc:creator")}\nMessage:Raw-Header:X-Received: ${metadata.get(
            "Message:Raw-Header:X-Received")}\nMessage:Raw-Header:Content-Language: ${metadata.get("Message:Raw-Header:Content-Language")}\nMessage:From-Email: ${metadata.get(
            "Message:From-Email")}\ndcterms:created: ${metadata.get("dcterms:created")}\nMessage-To: ${metadata.get("Message-To")}"
    }
}
