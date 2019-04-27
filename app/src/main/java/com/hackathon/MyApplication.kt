package com.hackathon

import com.hackathon.di.ILogger
import com.hackathon.di.appModule
import android.app.Application
import org.koin.android.ext.android.inject
import org.koin.android.ext.android.startKoin

class MyApplication : Application() {
    private val logger: ILogger by inject()

    override fun onCreate() {
        super.onCreate()

        startKoin(this, listOf(appModule))
    }
}