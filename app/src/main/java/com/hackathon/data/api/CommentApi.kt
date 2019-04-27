package com.hackathon.data.api

import com.hackathon.data.model.GetCommentRequest
import com.hackathon.data.model.GetCommentResponse
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST


interface CommentApi {
    @POST("/comments")
    fun getComments(@Body getCommentModel: GetCommentRequest): Single<Response<GetCommentResponse>>
}