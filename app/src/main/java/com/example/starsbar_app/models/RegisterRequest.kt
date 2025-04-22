package com.example.starsbar_app.models

data class RegisterRequest(
    val name: String,
    val lastname: String,
    val location: String,
    val mail: String,
    val pass: String,
    val rol: String
)