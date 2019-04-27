package com.hackathon.data.model

data class SignUpRequest(
    val userName: String,
    val email: String,
    val password: String,
    val firstName: String,
    val middleName: String?,
    val lastName: String
)