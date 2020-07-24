package com.cuupa.mailprocessor.services.semantic;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.List;

public class SemanticResult implements Serializable {

    private final String topicName;

    private String sender;

    private List<Metadata> metaData;

    public SemanticResult(String topicName) {
        this.topicName = topicName;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setMetaData(List<Metadata> metaData) {
        this.metaData = metaData;
    }

    public List<Metadata> getMetaData() {
        return metaData;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("topicName", topicName)
                                        .append("sender", sender)
                                        .append("metaData", metaData)
                                        .toString();
    }
}
