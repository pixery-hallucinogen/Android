package com.hackathon.data.model

data class PurchaseRequest(
        val userId: Int,
        val storeId: Int,
        val productId: Int,
        val amount: Int
)