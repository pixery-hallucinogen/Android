package com.hackathon.ui.splash

import android.content.Context
import com.hackathon.Constants
import com.hackathon.di.ILogger
import com.hackathon.di.module.SchedulersModule
import com.hackathon.lib.event.ObservableResult
import com.hackathon.lib.typing.Ok
import com.hackathon.ui.base.BaseViewModel
import com.hackathon.utils.PreferenceUtils

class SplashViewModel(
        private val logger: ILogger,
        schedulersModule: SchedulersModule
) : BaseViewModel(schedulersModule) {
    val onAuthResult = ObservableResult<Boolean>()

    fun isLoggedIn(context: Context) {
        val result = PreferenceUtils.defaultPrefs(context).getBoolean(Constants.AUTH_STATUS, false)
        onAuthResult.trigger(Ok(result))
    }
}
