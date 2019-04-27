package com.hackathon.di

interface ILogger {
    fun v(msg: String)

    fun d(msg: String)

    fun w(msg: String)

    fun e(msg: String)
}