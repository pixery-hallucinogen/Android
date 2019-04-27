package com.hackathon.data.error

import android.content.Context
import com.hackathon.lib.typing.Err

abstract class BaseError {

    protected abstract fun parseTitleAndMessage(context: Context): Pair<String, String>

    fun parseError(context: Context): Pair<String, String> =
        parseTitleAndMessage(context)

    fun toErr(): Err<BaseError> =
        Err(this)
}