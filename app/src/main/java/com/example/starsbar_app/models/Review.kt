package com.example.starsbar_app.models

import java.util.Date

data class Review(
    val id: Int,
    val name: String,
    val comment: String,
    val rating: Float,
    val created_at: Date
)