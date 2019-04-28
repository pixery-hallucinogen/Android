package com.hackathon.data.model

data class Post(
        val id: Int? = null,
        val latitude: Double,
        val longitude: Double,
        val media: String,
        val description: String? = null,
        val postDate: String? = null,
        val userId: String? = null,
        val location: String,
        var likeCount: Int? = null,
        var alreadyLiked: Boolean? = false,
        val userPhoto: String? = null,
        val userName: String? = null
)