package com.hackathon.data.api

import com.hackathon.data.model.CommentRequest
import com.hackathon.data.model.CreatePostRequest
import com.hackathon.data.model.GetPostsResponse
import com.hackathon.data.model.LikeRequest
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST


interface PostApi {
    @GET("/v1/posts")
    fun getPosts(): Single<Response<GetPostsResponse>>

    @POST("/v1/posts")
    fun createPost(@Body createPostRequest: CreatePostRequest): Single<Response<Unit>>

    @POST("/v1/posts")
    fun likePost(@Body likeRequest: LikeRequest): Single<Response<Unit>>

    @POST("/v1/posts")
    fun commentPost(@Body commentRequest: CommentRequest): Single<Response<Unit>>
}