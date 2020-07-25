package com.cuupa.mailprocessor.process

import org.camunda.bpm.engine.delegate.DelegateExecution
import java.util.*

abstract class AbstractProcessInstanceHandler(protected val delegateExecution: DelegateExecution) {

    //TODO: value?
    protected fun add(key: String, value: Any?) {
        if (delegateExecution.hasVariable(key)) {
            val variableCasted = getAsT<Map<String, Any>>(key, MutableMap::class.java)
            delegateExecution.setVariable(key, variableCasted)
        } else {
            delegateExecution.setVariable(key, ArrayList<Any>())
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

    protected fun <T> getAsListOf(propertyName: String): T {
        return getAsT(propertyName, MutableList::class.java)
    }

    protected fun getAsListOfString(propertyName: String): List<String> {
        return if (delegateExecution.hasVariable(propertyName)) {
            getAsT(propertyName, MutableList::class.java)
        } else ArrayList()
    }

    private fun <T> getAsT(propertyName: String, clazz: Class<*>): T {
        val property = delegateExecution.getVariable(propertyName)
        return if (clazz.isInstance(property)) {
            clazz.cast(property) as T
        } else {
            throw RuntimeException("Invalid data type")
        }
    }

    protected fun getAsMap(propertyName: String): Map<String, Any> {
        return if (delegateExecution.hasVariable(propertyName)) {
            getAsT(propertyName, MutableMap::class.java)
        } else HashMap()
    }

    protected fun getAsByteArray(propertyName: String): ByteArray {
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