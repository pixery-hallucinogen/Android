package com.hackathon.lib.typing

sealed class Result<out S, out E> {
    internal abstract val errVariant: Boolean
    internal abstract val okVariant: Boolean

    val isErr: Boolean
        get() = errVariant
    val isOk: Boolean
        get() = okVariant
}

data class Err<out E>(
        val error: E
) : Result<Nothing, E>() {
    override val errVariant: Boolean = true
    override val okVariant: Boolean = false

}

data class Ok<out S>(
        val value: S
) : Result<S, Nothing>() {
    override val errVariant: Boolean = false
    override val okVariant: Boolean = true
}

inline fun <S, E, C> Result<S, E>.fold(onOk: (S) -> C, onErr: (E) -> C): C = when (this) {
    is Err -> onErr(error)
    is Ok -> onOk(value)
}

inline fun <S, E, S2> Result<S, E>.map(fn: (S) -> S2): Result<S2, E> = when (this) {
    is Err -> this
    is Ok -> Ok(fn(value))
}

inline fun <S, E, S2> Result<S, E>.flatMap(fn: (S) -> Result<S2, E>): Result<S2, E> = when (this) {
    is Err -> this
    is Ok -> fn(value)
}

inline fun <S, E, E2> Result<S, E>.mapErr(fn: (E) -> E2): Result<S, E2> = when (this) {
    is Err -> Err(fn(error))
    is Ok -> this
}

fun <S, E> Result<S, E>.asNullable(): S? = when (this) {
    is Err -> null
    is Ok -> value
}

fun <S, E> Result<S, E>.asOptional(): Option<S> = when (this) {
    is Err -> None
    is Ok -> Some(value)
}

fun <S, E> Result<S, E>.expect(): S = when (this) {
    is Err -> throw RuntimeException("Result was expected to be of Ok type!")
    is Ok -> value
}