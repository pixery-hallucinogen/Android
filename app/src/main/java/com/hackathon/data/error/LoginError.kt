package com.hackathon.data.error

import com.hackathon.R
import com.hackathon.ui.util.fromContextString
import android.content.Context

class LoginError(val loginErrorType: LoginErrorType) : BaseError() {
    override fun parseTitleAndMessage(context: Context): Pair<String, String> = when (loginErrorType) {
        LoginErrorType.INVALID_INFO -> Pair(R.string.errorOccurred, R.string.invalidInfo).fromContextString(context)
        LoginErrorType.EMPTY_INFO -> Pair(R.string.errorOccurred, R.string.emptyInfo).fromContextString(context)
    }
}
