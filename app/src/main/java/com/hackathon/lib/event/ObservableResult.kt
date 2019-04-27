package com.hackathon.lib.event

import androidx.lifecycle.LifecycleOwner
import com.hackathon.data.error.BaseError
import com.hackathon.lib.typing.Result
import com.hackathon.lib.typing.fold

class ObservableResult<S> : ObservableDataEvent<Result<S, BaseError>>() {
    val loadSubscriber = ObservableDataEvent<Unit>()

    fun triggerLoad() {
        loadSubscriber.trigger(Unit)
    }

    inline fun runWhenLoading(owner: LifecycleOwner, crossinline loading: () -> Unit) {
        loadSubscriber.runWhenTriggered(owner) {
            loading()
        }
    }

    inline fun runWhenFinished(
        owner: LifecycleOwner,
        crossinline onSuccess: (S) -> Unit,
        crossinline onError: (BaseError) -> Unit
    ) {
        runWhenTriggered(owner) {
            it.fold(onOk = onSuccess, onErr = onError)
        }
    }
}