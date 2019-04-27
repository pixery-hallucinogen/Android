package com.hackathon.data.model

data class Product(
        val _id: String,
        val productId: Int,
        val productName: String,
        val tags: List<String>,
        val imageUrl: String,
        val price: String? = null,
        val currentPrice: String? = null,
        val taksit: String? = null
)