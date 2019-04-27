package com.hackathon.data.model

data class Comment(
        val _id: String,
        val commentId: Int,
        val userId: Int,
        val productId: Int,
        val commentBody: String,
        val star: Float,
        val date: String
)