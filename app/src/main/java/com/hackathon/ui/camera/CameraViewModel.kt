package com.hackathon.ui.camera

import com.hackathon.data.error.ServerError
import com.hackathon.data.model.GetCommentResponse
import com.hackathon.di.ILogger
import com.hackathon.di.module.SchedulersModule
import com.hackathon.lib.event.ObservableResult
import com.hackathon.lib.typing.Err
import com.hackathon.ui.base.BaseViewModel

class CameraViewModel(
        private val logger: ILogger,
        schedulersModule: SchedulersModule
) : BaseViewModel(schedulersModule) {
    val onCommentsFetched = ObservableResult<GetCommentResponse>()
    val onPurchased = ObservableResult<Unit>()

}
