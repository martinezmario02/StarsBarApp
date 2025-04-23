package com.example.starsbar_app.models

import java.io.Serializable

data class Restaurant(
    val id: Int,
    val name: String,
    val location: String,
    val description: String,
    val average_rating: Float,
    val mail: String,
    val phone: String,
    val image: String? = null,
    val pass: String
) : Serializable