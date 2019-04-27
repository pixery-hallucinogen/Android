package com.hackathon.di

import com.hackathon.Constants
import com.hackathon.data.repository.CommentRepository
import com.hackathon.data.repository.PurchaseRepository
import com.hackathon.data.repository.UserRepository
import com.hackathon.di.impl.AndroidLogger
import com.hackathon.di.module.ContextModule
import com.hackathon.di.module.RetrofitClient
import com.hackathon.di.module.SchedulersModule
import com.hackathon.domain.auth.AuthTask
import com.hackathon.domain.auth.CommentTask
import com.hackathon.domain.auth.PurchaseTask
import com.hackathon.ui.camera.CameraViewModel
import com.hackathon.ui.home.HomeViewModel
import com.hackathon.ui.login.LoginViewModel
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
    factory { CommentTask(get(), get()) }
    factory { PurchaseTask(get(), get()) }

    // Repositories
    factory { UserRepository(androidContext(), get(), get()) }
    factory { CommentRepository(androidContext(), get(), get()) }
    factory { PurchaseRepository(androidContext(), get(), get()) }

    // Retrofit
    single { RetrofitClient(androidContext()) }
    factory { get<RetrofitClient>().commentApi() }
    factory { get<RetrofitClient>().userApi() }
    factory { get<RetrofitClient>().purchaseApi() }

    // View Models
    viewModel { HomeViewModel(get(), get(), get()) }
    viewModel { SplashViewModel(get(), get()) }
    viewModel { LoginViewModel(get(), get(), get()) }
    viewModel { CameraViewModel(get(), get(), get(), get()) }
}