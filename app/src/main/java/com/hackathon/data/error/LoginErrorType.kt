package com.hackathon.data.error

enum class LoginErrorType {
    INVALID_INFO,
    EMPTY_INFO;

    fun toError(): LoginError =
            LoginError(this)
}