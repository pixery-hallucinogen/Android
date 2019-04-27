package com.hackathon.ui.login

import com.hackathon.data.error.LoginErrorType
import com.hackathon.di.ILogger
import com.hackathon.di.module.SchedulersModule
import com.hackathon.domain.auth.AuthTask
import com.hackathon.lib.event.ObservableResult
import com.hackathon.lib.typing.Err
import com.hackathon.ui.base.BaseViewModel

class LoginViewModel(
        private val logger: ILogger,
        schedulersModule: SchedulersModule,
        private val authTask: AuthTask
) : BaseViewModel(schedulersModule) {
    val onLoggedIn = ObservableResult<Unit>()

    var username: String? = null
    var password: String? = null

    fun isLoggedIn() {
        if (username.isNullOrBlank() || password.isNullOrBlank()) {
            onLoggedIn.trigger(Err(LoginErrorType.EMPTY_INFO.toError()))
        } else {
            subscribe(
                    event = onLoggedIn,
                    disposable = { authTask.login(username!!, password!!) },
                    onSuccess = { result ->
                        result
                    },
                    onError = {
                        Err(LoginErrorType.INVALID_INFO.toError())
                    }
            )
        }
    }
}
