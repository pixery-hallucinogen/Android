package com.hackathon.data.model

data class Post(
        val id: Int,
        val latitude: Double,
        val longitude: Double,
        val media: String,
        val description: String? = null,
        val postDate: String,
        val userId: String,
        val location: String,
        val likeCount: Int,
        val alreadyLiked: Boolean,
        val userPhoto: String? = null,
        val userName: String? = null
)