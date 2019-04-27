package com.hackathon.lib.event

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

open class ObservableDataEvent<T> : LiveData<DataEvent<T>>() {
    fun trigger(content: T) {
        this.value = DataEvent(content)
    }

    operator fun invoke(content: T) {
        this.value = DataEvent(content)
    }

    inline fun runWhenTriggered(owner: LifecycleOwner, crossinline fn: (T) -> Unit) {
        super.observe(owner, Observer { event ->
            event?.runWithDataIfNotHandled { fn(it) }
        })
    }
}