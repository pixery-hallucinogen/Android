package com.hackathon.data.repository

import android.content.Context
import com.hackathon.Constants
import com.hackathon.data.api.ApiResult
import com.hackathon.data.api.UserApi
import com.hackathon.data.error.LoginError
import com.hackathon.data.error.LoginErrorType
import com.hackathon.data.model.GetAccountResponse
import com.hackathon.data.model.LoginRequest
import com.hackathon.data.model.LoginResult
import com.hackathon.data.model.User
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
                PreferenceUtils.defaultPrefs(context)[Constants.JWT] = result.accessToken

                Ok(Unit).single()
            } else {
                LoginError(LoginErrorType.INVALID_INFO).toErr().single()
            }
        }
    }

    fun getAccount(): ApiResult<Unit> {
        return loginApi.getAccount().flatMap {
            if (it.isSuccessful && it.body() is GetAccountResponse) {
                val result = it.body() as GetAccountResponse
                PreferenceUtils.defaultPrefs(context)[Constants.USER_ID] = result.user.id
                PreferenceUtils.defaultPrefs(context)[Constants.USER_NAME] = result.user.userName
                PreferenceUtils.defaultPrefs(context)[Constants.USER_EMAIL] = result.user.email
                PreferenceUtils.defaultPrefs(context)[Constants.USER_FIRSTNAME] = result.user.firstName
                PreferenceUtils.defaultPrefs(context)[Constants.USER_MIDDLENAME] = result.user.middleName
                PreferenceUtils.defaultPrefs(context)[Constants.USER_LASTNAME] = result.user.lastName
                PreferenceUtils.defaultPrefs(context)[Constants.USER_IMAGE] = result.user.image

                Ok(Unit).single()
            } else {
                LoginError(LoginErrorType.INVALID_INFO).toErr().single()
            }
        }
    }
}