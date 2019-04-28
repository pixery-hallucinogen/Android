package com.hackathon.data.api

import com.hackathon.data.model.*
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path


interface PostApi {
    @GET("/v1/posts")
    fun getPosts(): Single<Response<GetPostsResponse>>

    @GET("/v1/posts/coordinates/{latitude}/{longitude}")
    fun getNearbyPosts(@Path("latitude") latitude: Float, @Path("longitude") longitude: Float): Single<Response<GetPostsResponse>>

    @GET("/v1/posts/comments/{postId}")
    fun getComments(@Path("postId") postId: Int): Single<Response<GetCommentResponse>>

    @POST("/v1/posts/create")
    fun createPost(@Body createPostRequest: CreatePostRequest): Single<Response<Unit>>

    @POST("/v1/posts/like")
    fun likePost(@Body likeRequest: LikeRequest): Single<Response<Unit>>

    @POST("/v1/posts/comment")
    fun commentPost(@Body commentRequest: CommentRequest): Single<Response<Unit>>
}