package com.hackathon.ui.camera

import com.hackathon.data.error.ServerError
import com.hackathon.data.model.GetCommentResponse
import com.hackathon.di.ILogger
import com.hackathon.di.module.SchedulersModule
import com.hackathon.domain.auth.CommentTask
import com.hackathon.lib.event.ObservableResult
import com.hackathon.lib.typing.Err
import com.hackathon.ui.base.BaseViewModel

class CameraViewModel(
        private val logger: ILogger,
        schedulersModule: SchedulersModule,
        private val commentTask: CommentTask
) : BaseViewModel(schedulersModule) {
    val onCommentsFetched = ObservableResult<GetCommentResponse>()
    val onPurchased = ObservableResult<Unit>()

    fun getComments(productId: Int, storeId: Int, shelfId: Int) {
        subscribe(
                event = onCommentsFetched,
                disposable = { commentTask.getComments(productId, storeId, shelfId) },
                onSuccess = { it },
                onError = { Err(ServerError(it.message ?: "Unknown Error")) }
        )
    }
}
