package com.hackathon.ui.home

import android.content.Context
import com.hackathon.data.error.ServerError
import com.hackathon.data.model.GetPostsResponse
import com.hackathon.di.ILogger
import com.hackathon.di.module.SchedulersModule
import com.hackathon.domain.auth.AuthTask
import com.hackathon.domain.auth.PostTask
import com.hackathon.lib.event.ObservableResult
import com.hackathon.lib.typing.Err
import com.hackathon.lib.typing.Ok
import com.hackathon.ui.base.BaseViewModel

class HomeViewModel(
        private val logger: ILogger,
        schedulersModule: SchedulersModule,
        private val authTask: AuthTask,
        private val postTask: PostTask
) : BaseViewModel(schedulersModule) {
    val onPostsFetched = ObservableResult<GetPostsResponse>()
    val onLoggedOut = ObservableResult<Unit>()

    fun getPosts() {
        subscribe(
                event = onPostsFetched,
                disposable = { postTask.getPosts() },
                onSuccess = { it },
                onError = { Err(ServerError(it.message ?: "Unknown Error")) }
        )
    }

    fun logout(context: Context) {
        authTask.logout(context)
        onLoggedOut.trigger(Ok(Unit))
    }
}
