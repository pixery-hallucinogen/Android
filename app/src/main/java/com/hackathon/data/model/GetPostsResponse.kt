package com.hackathon.data.model

data class GetPostsResponse(
        val posts: List<Post>,
        val statusCode: Int
)