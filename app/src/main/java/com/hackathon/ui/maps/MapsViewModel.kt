package com.hackathon.ui.maps

import com.hackathon.data.error.ServerError
import com.hackathon.data.model.CommentRequest
import com.hackathon.di.ILogger
import com.hackathon.di.module.SchedulersModule
import com.hackathon.domain.auth.AuthTask
import com.hackathon.domain.auth.PostTask
import com.hackathon.lib.event.ObservableResult
import com.hackathon.lib.typing.Err
import com.hackathon.lib.typing.Ok
import com.hackathon.ui.base.BaseViewModel

class MapsViewModel(
        private val logger: ILogger,
        schedulersModule: SchedulersModule,
        private val authTask: AuthTask,
        private val postTask: PostTask
) : BaseViewModel(schedulersModule) {
    val onLatLonReceived = ObservableResult<List<Float>>()
    val onPostCommented = ObservableResult<CommentRequest>()

    fun triggetLatLon(lat: Float, lon: Float) {
        onLatLonReceived.trigger(Ok(listOf(lat, lon)))
    }

    fun commentPost(postId: Int, comment: String) {
        subscribe(
                event = onPostCommented,
                disposable = { postTask.commentPost(postId, comment) },
                onSuccess = { it },
                onError = { Err(ServerError(it.message ?: "Unknown Error")) }
        )
    }
}
