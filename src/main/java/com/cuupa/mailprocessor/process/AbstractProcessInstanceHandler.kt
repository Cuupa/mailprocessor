package com.cuupa.mailprocessor.process

import org.camunda.bpm.engine.delegate.DelegateExecution
import java.util.*

abstract class AbstractProcessInstanceHandler(protected val delegateExecution: DelegateExecution) {
    //FIXME: this can't work
    protected fun add(key: String, value: Any?) {
        if (delegateExecution.hasVariable(key) && delegateExecution.getVariable(key) != null) {
            val variableCasted = getAsT<MutableMap<String, Any?>>(key)
            variableCasted[key] = value
            delegateExecution.setVariable(key, variableCasted)
        } else {
            delegateExecution.setVariable(key, listOf(value))
        }
    }

    protected fun addToList(key: String, value: Any?) {
        if (delegateExecution.hasVariable(key) && delegateExecution.getVariable(key) != null) {
            val variableCasted = getAsT<MutableList<Any?>>(key)
            if (value is Collection<*>) {
                if (value.isEmpty()) {
                    return
                }
                variableCasted.addAll(value)
            } else {
                variableCasted.add(value)
            }
            delegateExecution.setVariable(key, variableCasted)
        } else {
            delegateExecution.setVariable(key, mutableListOf(value))
        }
    }

    protected operator fun set(key: String?, value: Any?) {
        delegateExecution.setVariable(key, value)
    }

    protected fun getAsString(propertyName: String): String? {
        return if (delegateExecution.hasVariable(propertyName)) {
            getAsT<String>(propertyName)
        } else null
    }

    protected fun <T> getAsListOf(propertyName: String): List<T> {
        return getAsT<MutableList<T>>(propertyName)
    }

    protected fun getAsListOfString(propertyName: String): List<String> {
        return if (delegateExecution.hasVariable(propertyName)) {
            getAsT(propertyName)
        } else ArrayList()
    }

    private fun <T> getAsT(propertyName: String): T {
        return delegateExecution.getVariable(propertyName) as T
    }

    protected fun getAsMap(propertyName: String): Map<String, Any> {
        return if (delegateExecution.hasVariable(propertyName)) {
            getAsT(propertyName) ?: mapOf()
        } else HashMap()
    }

    protected fun getAsByteArray(propertyName: String): ByteArray? {
        return if (delegateExecution.hasVariable(propertyName)) {
            getAsT(propertyName)
        } else ByteArray(0)
    }

    protected fun getAsBooleanDefaultFalse(name: String): Boolean {
        return if (delegateExecution.hasVariable(name)) {
            return delegateExecution.getVariable(name) as Boolean? ?: false
        } else {
            false
        }
    }

    init {
        for (property in ProcessProperty.values()) {
            if (!delegateExecution.hasVariable(property.name)) {
                delegateExecution.setVariable(property.name, null)
            }
        }
    }
}