package com.hackathon.domain.auth

import android.content.Context
import com.hackathon.Constants
import com.hackathon.data.error.BaseError
import com.hackathon.data.error.LoginError
import com.hackathon.data.error.LoginErrorType
import com.hackathon.data.error.ServerError
import com.hackathon.data.repository.UserRepository
import com.hackathon.di.ILogger
import com.hackathon.domain.base.BaseTask
import com.hackathon.lib.typing.Ok
import com.hackathon.lib.typing.SingleResult
import com.hackathon.utils.PreferenceUtils
import com.hackathon.utils.set


class AuthTask(
        private val logger: ILogger,
        private val userRepository: UserRepository
) : BaseTask() {
    fun login(username: String, password: String): SingleResult<Unit, BaseError> {
        return userRepository.login(username, password).map {
            if (it.isOk)
                Ok(Unit)
            else
                LoginError(LoginErrorType.INVALID_INFO).toErr()
        }
    }

    fun getAccount(): SingleResult<Unit, BaseError> {
        return userRepository.getAccount().map {
            if (it.isOk)
                Ok(Unit)
            else
                ServerError("Error occurred").toErr()
        }
    }

    fun logout(context: Context) {
        PreferenceUtils.defaultPrefs(context)[Constants.AUTH_STATUS] = false
        PreferenceUtils.defaultPrefs(context)[Constants.USERNAME] = null
        PreferenceUtils.defaultPrefs(context)[Constants.UID] = null
        PreferenceUtils.defaultPrefs(context)[Constants.USER_ID] = null
        PreferenceUtils.defaultPrefs(context)[Constants.USER_NAME] = null
        PreferenceUtils.defaultPrefs(context)[Constants.USER_EMAIL] = null
        PreferenceUtils.defaultPrefs(context)[Constants.USER_FIRSTNAME] = null
        PreferenceUtils.defaultPrefs(context)[Constants.USER_MIDDLENAME] = null
        PreferenceUtils.defaultPrefs(context)[Constants.USER_LASTNAME] = null
        PreferenceUtils.defaultPrefs(context)[Constants.USER_IMAGE] = null
    }
}

