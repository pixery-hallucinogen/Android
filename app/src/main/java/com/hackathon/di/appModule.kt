package com.hackathon.di

import com.hackathon.Constants
import com.hackathon.data.repository.PostRepository
import com.hackathon.data.repository.UserRepository
import com.hackathon.di.impl.AndroidLogger
import com.hackathon.di.module.ContextModule
import com.hackathon.di.module.RetrofitClient
import com.hackathon.di.module.SchedulersModule
import com.hackathon.domain.auth.AuthTask
import com.hackathon.domain.auth.PostTask
import com.hackathon.ui.comments.CommentsViewModel
import com.hackathon.ui.home.HomeViewModel
import com.hackathon.ui.login.LoginViewModel
import com.hackathon.ui.maps.MapsViewModel
import com.hackathon.ui.splash.SplashViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val appModule = module {
    // Singletons
    single { ContextModule(androidContext()) }

    // Factories
    factory<ILogger> { AndroidLogger(Constants.TAG) }
    factory { SchedulersModule() }

    // Use Cases
    factory { AuthTask(get(), get()) }
    factory { PostTask(get(), get()) }

    // Repositories
    factory { UserRepository(androidContext(), get(), get()) }
    factory { PostRepository(androidContext(), get(), get()) }

    // Retrofit
    single { RetrofitClient(androidContext()) }
    factory { get<RetrofitClient>().userApi() }
    factory { get<RetrofitClient>().postApi() }

    // View Models
    viewModel { HomeViewModel(get(), get(), get(), get()) }
    viewModel { SplashViewModel(get(), get()) }
    viewModel { LoginViewModel(get(), get(), get()) }
    viewModel { CommentsViewModel(get(), get(), get(), get()) }
    viewModel { MapsViewModel(get(), get(), get(), get()) }
}