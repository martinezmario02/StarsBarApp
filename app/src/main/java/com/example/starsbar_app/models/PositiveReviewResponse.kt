package com.example.starsbar_app.models

data class PositiveReviewResponse(
    val restaurantId: Int,
    val positivePercentage: String,
    val positiveCount: Int,
    val totalCount: Int
)