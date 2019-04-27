package com.hackathon.data.error

import android.content.Context
import com.hackathon.R

class ServerError(val message: String) : BaseError() {
    override fun parseTitleAndMessage(context: Context): Pair<String, String> =
        Pair(context.getString(R.string.errorOccurred), message)
}

