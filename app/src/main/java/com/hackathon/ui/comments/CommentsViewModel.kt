package com.hackathon.ui.comments

import com.hackathon.data.error.ServerError
import com.hackathon.data.model.CommentRequest
import com.hackathon.data.model.GetCommentResponse
import com.hackathon.di.ILogger
import com.hackathon.di.module.SchedulersModule
import com.hackathon.domain.auth.AuthTask
import com.hackathon.domain.auth.PostTask
import com.hackathon.lib.event.ObservableResult
import com.hackathon.lib.typing.Err
import com.hackathon.ui.base.BaseViewModel

class CommentsViewModel(
        private val logger: ILogger,
        schedulersModule: SchedulersModule,
        private val authTask: AuthTask,
        private val postTask: PostTask
) : BaseViewModel(schedulersModule) {
    val onCommentsFetched = ObservableResult<GetCommentResponse>()
    val onPostCommented = ObservableResult<CommentRequest>()

    fun getComments(postId: Int) {
        subscribe(
                event = onCommentsFetched,
                disposable = { postTask.getComments(postId) },
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
}
