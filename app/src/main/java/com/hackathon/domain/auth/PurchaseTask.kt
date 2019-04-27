package com.hackathon.domain.auth

import com.hackathon.data.error.BaseError
import com.hackathon.data.repository.PurchaseRepository
import com.hackathon.di.ILogger
import com.hackathon.domain.base.BaseTask
import com.hackathon.lib.typing.SingleResult
import com.hackathon.lib.typing.single


class PurchaseTask(
        private val logger: ILogger,
        private val purchaseRepository: PurchaseRepository
) : BaseTask() {
    fun purchase(productId: Int, storeId: Int, amount: Int): SingleResult<Unit, BaseError> {
        return purchaseRepository.purchase(productId, storeId, amount).flatMap {
            it.single()
        }
    }
}

