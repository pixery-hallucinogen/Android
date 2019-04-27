package com.hackathon.domain.auth

import com.hackathon.data.error.BaseError
import com.hackathon.data.model.GetCommentResponse
import com.hackathon.data.repository.CommentRepository
import com.hackathon.di.ILogger
import com.hackathon.domain.base.BaseTask
import com.hackathon.lib.typing.SingleResult
import com.hackathon.lib.typing.single


class CommentTask(
        private val logger: ILogger,
        private val commentRepository: CommentRepository
) : BaseTask() {
    fun getComments(productId: Int, storeId: Int, shelfId: Int): SingleResult<GetCommentResponse, BaseError> {
        return commentRepository.getComments(productId, storeId, shelfId).flatMap {
            it.single()
        }
    }
}

