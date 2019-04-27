package com.hackathon.data.model

data class GetCommentRequest(
        val userId: Int,
        val storeId: Int,
        val productId: Int,
        val shelfNumber: Int
)