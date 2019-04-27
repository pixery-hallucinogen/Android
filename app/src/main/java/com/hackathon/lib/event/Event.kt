package com.hackathon.lib.event

sealed class Event {
    var hasBeenHandled = false
        protected set

    fun <T> runIfNotHandled(fn: () -> T): T? = when (hasBeenHandled) {
        true -> null
        else -> {
            hasBeenHandled = true
            fn()
        }
    }
}

class DataEvent<out T>(
        private val content: T
) : Event() {
    fun <U> runWithDataIfNotHandled(fn: (T) -> U): U? = when (hasBeenHandled) {
        true -> null
        else -> {
            hasBeenHandled = true
            fn(content)
        }
    }

    fun peekContent(): T {
        return content
    }
}