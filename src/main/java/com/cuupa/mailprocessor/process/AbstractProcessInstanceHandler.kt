package com.cuupa.mailprocessor.process

import org.camunda.bpm.engine.delegate.DelegateExecution
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.ArrayList

abstract class AbstractProcessInstanceHandler(protected val delegateExecution: DelegateExecution) {
    //FIXME: this can't work
    protected fun add(property: ProcessProperty, value: Any?) {
        if (delegateExecution.hasVariable(property.name) && delegateExecution.getVariable(property.name) != null) {
            val variableCasted = getAsT<MutableMap<String, Any?>>(property)
            variableCasted[property.name] = value
            delegateExecution.setVariable(property.name, variableCasted)
        } else {
            delegateExecution.setVariable(property.name, listOf(value))
        }
    }

    protected fun addToList(property: ProcessProperty, value: Any?) {
        if (delegateExecution.hasVariable(property.name) && delegateExecution.getVariable(property.name) != null) {
            val variableCasted = getAsT<MutableList<Any?>>(property)
            delegateExecution.setVariable(property.name, addIfApplicable(value, variableCasted))
        } else {
            val list = mutableListOf<Any?>()
            delegateExecution.setVariable(property.name, addIfApplicable(value, list))
        }
    }

    private fun addIfApplicable(value: Any?, list: MutableList<Any?>): MutableList<Any?> {
        if (value is Collection<*>) {
            if (value.isEmpty()) {
                return list
            }
            list.addAll(value)
        } else {
            list.add(value)
        }
        return list
    }

    protected operator fun set(property: ProcessProperty, value: Any?) {
        delegateExecution.setVariable(property.name, value)
    }

    protected fun getAsString(propertyName: ProcessProperty): String? {
        return if (delegateExecution.hasVariable(propertyName.name)) {
            getAsT<String>(propertyName)
        } else null
    }

    protected fun getAsLocalDateTime(propertyName: ProcessProperty): LocalDateTime? {
        return if (delegateExecution.hasVariable(propertyName.name)) {
            getAsT<LocalDateTime>(propertyName)
        } else null
    }

    protected fun <T> getAsListOf(propertyName: ProcessProperty): List<T> {
        return getAsT<MutableList<T>?>(propertyName) ?: mutableListOf()
    }

    protected fun getAsListOfString(propertyName: ProcessProperty): List<String> {
        return if (delegateExecution.hasVariable(propertyName.name)) {
            getAsT(propertyName) ?: ArrayList()
        } else ArrayList()
    }

    private fun <T> getAsT(propertyName: ProcessProperty): T {
        return delegateExecution.getVariable(propertyName.name) as T
    }

    protected fun getAsMap(propertyName: ProcessProperty): Map<String, Any> {
        return if (delegateExecution.hasVariable(propertyName.name)) {
            getAsT(propertyName) ?: mapOf()
        } else HashMap()
    }

    protected fun getAsByteArray(propertyName: ProcessProperty): ByteArray? {
        return if (delegateExecution.hasVariable(propertyName.name)) {
            getAsT(propertyName)
        } else ByteArray(0)
    }

    protected fun getAsBooleanDefaultFalse(propertyName: ProcessProperty): Boolean {
        return if (delegateExecution.hasVariable(propertyName.name)) {
            return delegateExecution.getVariable(propertyName.name) as Boolean? ?: false
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