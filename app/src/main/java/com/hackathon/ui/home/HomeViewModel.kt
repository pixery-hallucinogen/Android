package com.hackathon.ui.home

import android.content.Context
import com.hackathon.data.error.ServerError
import com.hackathon.data.model.CommentRequest
import com.hackathon.data.model.GetPostsResponse
import com.hackathon.data.model.LikeRequest
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
    val onNearbyPostsFetched = ObservableResult<GetPostsResponse>()
    val onPostsFetched = ObservableResult<GetPostsResponse>()
    val onUserFetched = ObservableResult<Unit>()
    val onPostLiked = ObservableResult<LikeRequest>()
    val onPostCommented = ObservableResult<CommentRequest>()
    val onLoggedOut = ObservableResult<Unit>()

    fun getPosts() {
        subscribe(
                event = onPostsFetched,
                disposable = { postTask.getPosts() },
                onSuccess = { it },
                onError = { Err(ServerError(it.message ?: "Unknown Error")) }
        )
    }

    fun getNearbyPosts(lat: Float, lon: Float) {
        subscribe(
                event = onNearbyPostsFetched,
                disposable = { postTask.getNearbyPosts(lat, lon) },
                onSuccess = { it },
                onError = { Err(ServerError(it.message ?: "Unknown Error")) }
        )
    }

    fun likePost(postId: Int) {
        subscribe(
                event = onPostLiked,
                disposable = { postTask.likePost(postId) },
                onSuccess = { it },
                onError = { Err(ServerError(it.message ?: "Unknown Error")) }
        )
    }

    fun commentPost(postId: Int, comment: String) {
        subscribe(
                event = onPostCommented,
                disposable = { postTask.commentPost(postId, comment) },
                onSuccess = { it },
                onError = { Err(ServerError(it.message ?: "Unknown Error")) }
        )
    }

    fun getAccount() {
        subscribe(
                event = onUserFetched,
                disposable = { authTask.getAccount() },
                onSuccess = { it },
                onError = { Err(ServerError(it.message ?: "Unknown Error")) }
        )
    }

    fun logout(context: Context) {
        authTask.logout(context)
        onLoggedOut.trigger(Ok(Unit))
    }
}
