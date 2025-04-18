package com.example.starsbar_app.controllers

import com.example.starsbar_app.models.Restaurant
import com.example.starsbar_app.models.Review
import com.example.starsbar_app.models.UserNameResponse
import com.example.starsbar_app.network.ApiService

class UserController {

    private val apiService = ApiService.create()

    suspend fun getUserName(id: Int): UserNameResponse {
        return apiService.getUserName(id)
    }
}
