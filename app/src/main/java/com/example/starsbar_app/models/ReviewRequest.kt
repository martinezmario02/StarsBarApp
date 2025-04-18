package com.example.starsbar_app.models

data class ReviewRequest(
    val rating: Float,
    val comment: String,
    val user_id: Int
)