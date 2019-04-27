package com.hackathon.data.api

import com.hackathon.data.model.GetPostsResponse
import com.hackathon.data.model.PurchaseRequest
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET


interface PostApi {
    @GET("/v1/posts")
    fun purchase(@Body purchaseModel: PurchaseRequest): Single<Response<GetPostsResponse>>
}