package com.example.starsbar_app.models

import java.util.Date

data class Review(
    val id: Int,
    val user_id: Int,
    val rest_id: Int,
    val rating: Float,
    val comment: String,
    val created_at: Date
)