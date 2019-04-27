package com.hackathon.ui.home

import android.content.Context
import com.hackathon.data.model.Post
import com.hackathon.di.ILogger
import com.hackathon.di.module.SchedulersModule
import com.hackathon.domain.auth.AuthTask
import com.hackathon.lib.event.ObservableResult
import com.hackathon.lib.typing.Ok
import com.hackathon.ui.base.BaseViewModel
import java.util.*
import java.util.Arrays.asList

class HomeViewModel(
        private val logger: ILogger,
        schedulersModule: SchedulersModule,
        private val authTask: AuthTask
) : BaseViewModel(schedulersModule) {
    val onLoggedOut = ObservableResult<Unit>()
    val onRecommendationsFetched = ObservableResult<List<Post>>()

    fun logout(context: Context) {
        authTask.logout(context)
        onLoggedOut.trigger(Ok(Unit))
    }

    fun fetchRecommendations() {
        val data = listOf(
                Post(1, 30.0, 30.0, "https://avatars1.githubusercontent.com/u/50017268?s=200&v=4", "Hello, world!", Date(), "", "Koç university", 10, true  )
        )

        onRecommendationsFetched.trigger(Ok(data))
    }
}
