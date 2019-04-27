package com.hackathon.lib.typing

sealed class Option<out T> {
    internal abstract val someVariant: Boolean

    val isNone: Boolean
        get() = !someVariant
    val isSome: Boolean
        get() = someVariant
}

object None : Option<Nothing>() {
    override val someVariant: Boolean = false
}

data class Some<out T>(
        val value: T
) : Option<T>() {
    override val someVariant: Boolean = true
}

inline fun <S, C> Option<S>.fold(onSome: (S) -> C, onNone: () -> C): C = when (this) {
    is None -> onNone()
    is Some -> onSome(value)
}

inline fun <S, S2> Option<S>.map(fn: (S) -> S2): Option<S2> = when (this) {
    is None -> this
    is Some -> Some(fn(value))
}

inline fun <S, S2> Option<S>.flatMap(fn: (S) -> Option<S2>): Option<S2> = when (this) {
    is None -> this
    is Some -> fn(value)
}

inline fun <S, E> Option<S>.mapNone(fn: () -> Result<S, E>): Result<S, E> = when (this) {
    is None -> fn()
    is Some -> Ok(value)
}

fun <S> Option<S>.asNullable(): S? = when (this) {
    is None -> null
    is Some -> value
}

fun <S> Option<S>.expect(): S = when (this) {
    is None -> throw RuntimeException("Option was expected to be of Some type!")
    is Some -> value
}