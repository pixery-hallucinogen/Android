package com.hackathon.data.model

import android.location.Location
import java.util.*

data class Post(
        val id: Int,
        val latitude: Double,
        val longitude: Double,
        val media: String,
        val description: String? = null,
        val postDate: Date,
        val userId: String,
        val location: Location,
        val likeCount: Int,
        val alreadyLiked: Boolean

)