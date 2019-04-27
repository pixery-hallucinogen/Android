package com.hackathon.ui.base

import com.hackathon.data.error.BaseError
import com.hackathon.di.module.SchedulersModule
import com.hackathon.lib.event.ObservableResult
import com.hackathon.lib.typing.Result
import androidx.lifecycle.ViewModel
import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable

abstract class BaseViewModel(
        val schedulersModule: SchedulersModule
) : ViewModel() {
    protected val disposables = CompositeDisposable()

    fun onScreenCreated() {}

    override fun onCleared() {
        disposables.clear()
    }

    protected inline fun <S, X> subscribe(
            event: ObservableResult<S>,
            disposable: () -> Single<out X>,
            crossinline onSuccess: (X) -> Result<S, BaseError>,
            crossinline onError: (Throwable) -> Result<S, BaseError>
    ) {
        disposables.add(
                disposable()
                        .subscribeOn(schedulersModule.io())
                        .observeOn(schedulersModule.ui())
                        .doOnSubscribe { event.triggerLoad() }
                        .subscribe({ result ->
                            event.trigger(onSuccess(result))
                        }, { error ->
                            event.trigger(onError(error))
                        })
        )
    }

    protected inline fun <S, X> subscribeMaybe(
            event: ObservableResult<S>,
            disposable: () -> Maybe<out X>,
            crossinline onSuccess: (X?) -> Result<S, BaseError>,
            crossinline onError: (Throwable) -> Result<S, BaseError>
    ) {
        disposables.add(
                disposable()
                        .subscribeOn(schedulersModule.io())
                        .observeOn(schedulersModule.ui())
                        .doOnSubscribe { event.triggerLoad() }
                        .subscribe({ result ->
                            event.trigger(onSuccess(result))
                        }, { error ->
                            event.trigger(onError(error))
                        }, {
                            event.trigger(onSuccess(null))
                        })
        )
    }
}