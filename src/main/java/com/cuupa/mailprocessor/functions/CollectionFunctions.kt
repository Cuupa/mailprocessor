package com.cuupa.mailprocessor.functions

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

suspend fun <A> Iterable<A>.forEachParallel(function: suspend (A) -> Unit) = coroutineScope {
    forEach { async { function(it) } }
}

suspend fun <A, B> Iterable<A>.mapParallel(function: suspend (A) -> B): List<B> = coroutineScope {
    map { async { function(it) } }.awaitAll()
}

suspend fun <A> Iterable<A>.filterParallel(function: suspend (A) -> Boolean): List<A> {
    val list = mutableListOf<Deferred<Boolean>>()
    var filtered = listOf<A>()
    coroutineScope {
        filtered = filter {
            list.add(async { function(it) })
        }
    }
    list.forEach { it.await() }
    return filtered
}