package com.cuupa.mailprocessor.process;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractProcessInstanceHandler {

    private final DelegateExecution delegateExecution;

    public AbstractProcessInstanceHandler(final DelegateExecution delegateExecution) {
        this.delegateExecution = delegateExecution;
        for(ProcessProperty property : ProcessProperty.values()) {
            if(!delegateExecution.hasVariable(property.name())){
                delegateExecution.setVariable(property.name(), null);
            }
        }
    }

    protected void add(final String key, final Object value) {
        if (delegateExecution.hasVariable(key)) {
            final Map<String, Object> variableCasted = getAsT(key, Map.class);
            delegateExecution.setVariable(key, variableCasted);
        } else {
            delegateExecution.setVariable(key, new ArrayList<>());
        }
    }

    protected void set(final String key, final Object value) {
        delegateExecution.setVariable(key, value);
    }

    protected String getAsString(final String propertyName) {
        if(delegateExecution.hasVariable(propertyName)){
            return getAsT(propertyName, String.class);
        }
        return null;
    }

    protected <T> T getAsListOf(final String propertyName){
        return getAsT(propertyName, List.class);
    }

    protected List<String> getAsListOfString(final String propertyName) {
        if (delegateExecution.hasVariable(propertyName)) {
            return getAsT(propertyName, List.class);
        }
        return new ArrayList<>();
    }

    @NotNull
    private <T> T getAsT(final String propertyName, Class clazz) {
        final Object property = delegateExecution.getVariable(propertyName);
        if (clazz.isInstance(property)) {
            return (T) clazz.cast(property);
        } else {
            throw new RuntimeException("Invalid data type");
        }
    }

    protected Map<String, Object> getAsMap(final String propertyName) {
        if(delegateExecution.hasVariable(propertyName)){
            return getAsT(propertyName, Map.class);
        }
        return new HashMap<>();
    }

    protected byte[] getAsByteArray(final String propertyName) {
        if (delegateExecution.hasVariable(propertyName)) {
            return getAsT(propertyName, byte[].class);
        }
        return new byte[0];
    }

    protected DelegateExecution getDelegateExecution(){
        return delegateExecution;
    }
}
