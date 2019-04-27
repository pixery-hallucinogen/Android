package com.hackathon.data.api

import com.hackathon.data.model.PurchaseRequest
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST


interface PurchaseApi {
    @POST("/purchase")
    fun purchase(@Body purchaseModel: PurchaseRequest): Single<Response<Unit>>
}