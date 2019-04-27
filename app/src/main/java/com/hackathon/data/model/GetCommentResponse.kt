package com.hackathon.data.model

data class GetCommentResponse(
        val product: List<Post>,
        val comments: List<CommentRequest>,
        val users: List<String>,
        val star: Float
)