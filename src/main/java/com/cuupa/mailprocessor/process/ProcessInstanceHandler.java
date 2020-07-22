package com.cuupa.mailprocessor.process;

import com.cuupa.mailprocessor.services.Metadata;
import org.camunda.bpm.engine.delegate.DelegateExecution;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProcessInstanceHandler extends AbstractProcessInstanceHandler {


    public ProcessInstanceHandler(final DelegateExecution delegateExecution) {
        super(delegateExecution);
    }

    public void setPlaintext(final List<String> textPerPage) {
        set(ProcessProperty.PLAIN_TEXT.name(), textPerPage);
    }

    public List<String> getPlainText(){
        return getAsListOfString(ProcessProperty.PLAIN_TEXT.name());
    }

    public byte[] getFileContent() {
        return getAsByteArray(ProcessProperty.FILE_CONTENT.name());
    }

    public ProcessInstanceHandler addTopic(final String topic) {
        add(ProcessProperty.TOPIC.name(), topic);
        return this;
    }

    public ProcessInstanceHandler setSender(final String sender) {
        set(ProcessProperty.SENDER.name(), sender);
        return this;
    }

    public String getSender(){
        return getAsString(ProcessProperty.SENDER.name());
    }

    public ProcessInstanceHandler addMetaData(final List<Metadata> metadata) {
        add(ProcessProperty.METADATA.name(), metadata);
        return this;
    }

    public List<Metadata> getMetadata(){
        return getAsListOf(ProcessProperty.METADATA.name());
    }

    public Map<String, Object> getDmnResult() {
        return getAsMap(ProcessProperty.DMN_RESULT.name());
    }

    public ProcessInstanceHandler setPathToSave(final String pathToSave) {
        add(ProcessProperty.PATH_TO_SAVE.name(), pathToSave);
        return this;
    }

    public ProcessInstanceHandler setHasReminder(final Boolean reminder) {
        add(ProcessProperty.HAS_REMINDER.name(), reminder);
        return this;
    }
}
