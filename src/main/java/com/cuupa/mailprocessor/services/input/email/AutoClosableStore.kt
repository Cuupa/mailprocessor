package com.cuupa.mailprocessor.services.input.email

import javax.mail.Store

class AutoClosableStore(private val store: Store) : AutoCloseable {
    override fun close() {
        store.close()
    }

    fun getStore(): Store {
        return store
    }
}