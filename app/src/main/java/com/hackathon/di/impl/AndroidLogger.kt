package com.hackathon.di.impl

import android.util.Log
import com.hackathon.di.ILogger

class AndroidLogger(
    private val tag: String
) : ILogger {
    override fun v(msg: String) {
        Log.v(tag, msg)
    }

    override fun d(msg: String) {
        Log.d(tag, msg)
    }

    override fun w(msg: String) {
        Log.w(tag, msg)
    }

    override fun e(msg: String) {
        Log.e(tag, msg)
    }

}