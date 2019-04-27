package com.hackathon.domain.auth

import com.hackathon.data.error.BaseError
import com.hackathon.data.error.LoginError
import com.hackathon.data.error.LoginErrorType
import com.hackathon.data.model.*
import com.hackathon.data.repository.PostRepository
import com.hackathon.di.ILogger
import com.hackathon.domain.base.BaseTask
import com.hackathon.lib.typing.Ok
import com.hackathon.lib.typing.SingleResult


class PostTask(
        private val logger: ILogger,
        private val postRepository: PostRepository
) : BaseTask() {
    fun getPosts(): SingleResult<GetPostsResponse, BaseError> {
        return postRepository.getPosts().map {
            if (it.isOk)
                it
            else
                LoginError(LoginErrorType.INVALID_INFO).toErr()
        }
    }

    fun getComments(postId: Int): SingleResult<GetCommentResponse, BaseError> {
        return postRepository.getComments(postId).map {
            if (it.isOk)
                it
            else
                LoginError(LoginErrorType.INVALID_INFO).toErr()
        }
    }

    fun commentPost(postId: Int, comment: String): SingleResult<CommentRequest, BaseError> {
        return postRepository.commentPost(CommentRequest(postId, comment)).map {
            if (it.isOk)
                Ok(CommentRequest(postId, comment))
            else
                LoginError(LoginErrorType.INVALID_INFO).toErr()
        }
    }

    fun likePost(postId: Int): SingleResult<LikeRequest, BaseError> {
        return postRepository.likePost(LikeRequest(postId)).map {
            if (it.isOk)
                Ok(LikeRequest(postId))
            else
                LoginError(LoginErrorType.INVALID_INFO).toErr()
        }
    }

    fun createPost(createPostRequest: CreatePostRequest): SingleResult<Unit, BaseError> {
        return postRepository.createPost(createPostRequest).map {
            if (it.isOk)
                Ok(Unit)
            else
                LoginError(LoginErrorType.INVALID_INFO).toErr()
        }
    }
}

