package com.hackathon.ui.util

import android.content.Context

fun Pair<Int, Int>.fromContextString(context: Context): Pair<String, String> {
    return Pair(context.getString(first), context.getString(second))
}