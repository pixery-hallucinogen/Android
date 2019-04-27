package com.hackathon.data.api

import com.hackathon.data.model.GetAccountResponse
import com.hackathon.data.model.LoginRequest
import com.hackathon.data.model.LoginResult
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST


interface UserApi {
    @POST("/v1/account/login")
    fun login(@Body loginModel: LoginRequest): Single<Response<LoginResult>>

    @GET("/v1/account")
    fun getAccount(): Single<Response<GetAccountResponse>>
}