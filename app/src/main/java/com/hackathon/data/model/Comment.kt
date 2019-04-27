package com.hackathon.data.model

data class Comment(
        val id: Int,
        val comment: String,
        val commentDate: String,
        val userPhoto: String?,
        val userName: String,
        val userId: String,
        val postId: Int
)