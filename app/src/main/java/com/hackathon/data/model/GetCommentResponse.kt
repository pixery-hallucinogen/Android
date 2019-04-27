package com.hackathon.data.model

data class GetCommentResponse(
        val comments: List<Comment>,
        val statusCode: Int
)