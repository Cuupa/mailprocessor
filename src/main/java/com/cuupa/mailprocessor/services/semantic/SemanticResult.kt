package com.cuupa.mailprocessor.services.semantic

import org.apache.commons.lang3.builder.ToStringBuilder
import java.io.Serializable

class SemanticResult(val topicName: String, val sender: String) : Serializable {

    var metaData: MutableList<Metadata> = mutableListOf()

    override fun toString(): String {
        return ToStringBuilder(this).append("topicName", topicName)
                .append("sender", sender)
                .append("metaData", metaData)
                .toString()
    }

}