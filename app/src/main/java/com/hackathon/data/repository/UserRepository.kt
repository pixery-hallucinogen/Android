package com.hackathon.data.repository

import android.content.Context
import com.hackathon.Constants
import com.hackathon.data.api.ApiResult
import com.hackathon.data.api.UserApi
import com.hackathon.data.error.LoginError
import com.hackathon.data.error.LoginErrorType
import com.hackathon.data.model.LoginRequest
import com.hackathon.data.model.LoginResult
import com.hackathon.di.ILogger
import com.hackathon.lib.typing.Ok
import com.hackathon.lib.typing.single
import com.hackathon.utils.PreferenceUtils
import com.hackathon.utils.set


class UserRepository(
        private val context: Context,
        private val logger: ILogger,
        private val loginApi: UserApi
) {
    fun login(username: String, password: String): ApiResult<Unit> {
        return loginApi.login(LoginRequest(username, password)).flatMap {
            if (it.isSuccessful && it.body() is LoginResult) {
                val result = it.body() as LoginResult
                PreferenceUtils.defaultPrefs(context)[Constants.AUTH_STATUS] = true
                PreferenceUtils.defaultPrefs(context)[Constants.USERNAME] = result.userName
                PreferenceUtils.defaultPrefs(context)[Constants.UID] = result.userId

                Ok(Unit).single()
            } else {
                LoginError(LoginErrorType.INVALID_INFO).toErr().single()
            }
        }
    }
}