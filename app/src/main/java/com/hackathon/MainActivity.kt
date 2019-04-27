package com.hackathon

import com.hackathon.di.ILogger
import com.hackathon.ui.base.BaseActivity
import android.os.Bundle
import org.koin.android.ext.android.inject

class MainActivity : BaseActivity() {
    private val logger: ILogger by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
    }
}