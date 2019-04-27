package com.hackathon.di.module

import android.content.Context
import com.hackathon.MyApplication

class ContextModule(
    private val applicationContext: Context
) {
    fun getContext(): Context {
        return applicationContext
    }

    fun getApplication(): MyApplication {
        return applicationContext.applicationContext as MyApplication
    }
}