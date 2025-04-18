package com.example.starsbar_app.models

data class LoginResponse(
    val success: Boolean,
    val message: String,
    val token: String? = null
)