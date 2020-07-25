package com.cuupa.mailprocessor.process

import org.camunda.bpm.engine.delegate.DelegateExecution
import java.util.*

abstract class AbstractProcessInstanceHandler(protected val delegateExecution: DelegateExecution) {

    protected fun add(key: String, value: Any?) {
        if (delegateExecution.hasVariable(key) && delegateExecution.getVariable(key) != null) {
            val variableCasted = getAsT<Map<String, Any?>>(key, MutableMap::class.java) as
                    MutableMap
            variableCasted[key] = value
            delegateExecution.setVariable(key, variableCasted)
        } else {
            delegateExecution.setVariable(key, listOf(value))
        }
    }

    protected operator fun set(key: String?, value: Any?) {
        delegateExecution.setVariable(key, value)
    }

    protected fun getAsString(propertyName: String): String? {
        return if (delegateExecution.hasVariable(propertyName)) {
            getAsT<String>(propertyName, String::class.java)
        } else null
    }

    protected fun <T> getAsListOf(propertyName: String): List<T> {
        return getAsT(propertyName, MutableList::class.java) ?: mutableListOf()
    }

    protected fun getAsListOfString(propertyName: String): List<String> {
        return if (delegateExecution.hasVariable(propertyName)) {
            getAsT(propertyName, MutableList::class.java) ?: mutableListOf()
        } else ArrayList()
    }

    private fun <T> getAsT(propertyName: String, clazz: Class<*>): T? {
        val property = delegateExecution.getVariable(propertyName)
        return if (clazz.isInstance(property)) {
            clazz.cast(property) as T
        } else {
            return null
        }
    }

    protected fun getAsMap(propertyName: String): Map<String, Any> {
        return if (delegateExecution.hasVariable(propertyName)) {
            getAsT(propertyName, MutableMap::class.java) ?: mapOf()
        } else HashMap()
    }

    protected fun getAsByteArray(propertyName: String): ByteArray? {
        return if (delegateExecution.hasVariable(propertyName)) {
            getAsT(propertyName, ByteArray::class.java)
        } else ByteArray(0)
    }

    init {
        for (property in ProcessProperty.values()) {
            if (!delegateExecution.hasVariable(property.name)) {
                delegateExecution.setVariable(property.name, null)
            }
        }
    }
}