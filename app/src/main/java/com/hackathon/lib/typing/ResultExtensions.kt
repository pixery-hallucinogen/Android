package com.hackathon.lib.typing

import io.reactivex.Maybe
import io.reactivex.Single

typealias SingleResult<S, E> = Single<Result<S, E>>
typealias MaybeResult<S, E> = Maybe<Result<S, E>>

fun <S, E> Result<S, E>.single(): SingleResult<S, E> = Single.just(this)
fun <S, E> Result<S, E>.maybe(): MaybeResult<S, E> = Maybe.just(this)

fun <S, E> Result<Option<S>, E>.transpose(): Option<Result<S, E>> = when (this) {
    is Err -> Some(this)
    is Ok -> when (value) {
        is None -> None
        is Some -> Some(Ok(this.value.value))
    }
}