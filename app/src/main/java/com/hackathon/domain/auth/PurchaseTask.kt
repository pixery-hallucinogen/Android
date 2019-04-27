package com.hackathon.domain.auth

import com.hackathon.data.error.BaseError
import com.hackathon.data.repository.PostRepository
import com.hackathon.di.ILogger
import com.hackathon.domain.base.BaseTask
import com.hackathon.lib.typing.SingleResult
import com.hackathon.lib.typing.single


class PurchaseTask(
        private val logger: ILogger,
        private val postRepository: PostRepository
) : BaseTask() {
    fun purchase(productId: Int, storeId: Int, amount: Int): SingleResult<Unit, BaseError> {
        return postRepository.purchase(productId, storeId, amount).flatMap {
            it.single()
        }
    }
}

