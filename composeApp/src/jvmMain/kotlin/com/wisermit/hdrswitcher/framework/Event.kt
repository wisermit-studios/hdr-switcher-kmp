package com.wisermit.hdrswitcher.framework

import kotlinx.coroutines.flow.MutableStateFlow

data class Event<T>(
    private var value: T?,
) {
    fun pickValue(): T? = value.also { value = null }
}

fun <T> MutableStateFlow<Event<T>?>.trySend(value: T): Boolean {
    return tryEmit(Event(value))
}