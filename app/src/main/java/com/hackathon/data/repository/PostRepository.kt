package com.hackathon.data.repository

import android.content.Context
import com.hackathon.R
import com.hackathon.data.api.ApiResult
import com.hackathon.data.api.PostApi
import com.hackathon.data.error.ServerError
import com.hackathon.data.model.*
import com.hackathon.di.ILogger
import com.hackathon.lib.typing.Ok
import com.hackathon.lib.typing.single


class PostRepository(
        private val context: Context,
        private val logger: ILogger,
        private val postApi: PostApi
) {
    fun getPosts(): ApiResult<GetPostsResponse> {
        return postApi.getPosts().flatMap {
            if (it.isSuccessful) {
                Ok(it.body() as GetPostsResponse).single()
            } else {
                ServerError(context.getString(R.string.errorOccurred)).toErr().single()
            }
        }
    }

    fun getNearbyPosts(lat: Float, lon: Float): ApiResult<GetPostsResponse> {
        return postApi.getNearbyPosts(lat, lon).flatMap {
            if (it.isSuccessful) {
                Ok(it.body() as GetPostsResponse).single()
            } else {
                ServerError(context.getString(R.string.errorOccurred)).toErr().single()
            }
        }
    }

    fun getComments(postId: Int): ApiResult<GetCommentResponse> {
        return postApi.getComments(postId).flatMap {
            if (it.isSuccessful) {
                Ok(it.body() as GetCommentResponse).single()
            } else {
                ServerError(context.getString(R.string.errorOccurred)).toErr().single()
            }
        }
    }

    fun commentPost(commentRequest: CommentRequest): ApiResult<Unit> {
        return postApi.commentPost(commentRequest).flatMap {
            if (it.isSuccessful) {
                Ok(Unit).single()
            } else {
                ServerError(context.getString(R.string.errorOccurred)).toErr().single()
            }
        }
    }

    fun likePost(likeRequest: LikeRequest): ApiResult<Unit> {
        return postApi.likePost(likeRequest).flatMap {
            if (it.isSuccessful) {
                Ok(Unit).single()
            } else {
                ServerError(context.getString(R.string.errorOccurred)).toErr().single()
            }
        }
    }

    fun createPost(createPostRequest: CreatePostRequest): ApiResult<Unit> {
        return postApi.createPost(createPostRequest).flatMap {
            if (it.isSuccessful) {
                Ok(Unit).single()
            } else {
                ServerError(context.getString(R.string.errorOccurred)).toErr().single()
            }
        }
    }
}