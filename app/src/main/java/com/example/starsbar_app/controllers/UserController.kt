package com.example.starsbar_app.controllers

import com.example.starsbar_app.models.ApiResponse
import com.example.starsbar_app.models.LoginRequest
import com.example.starsbar_app.models.LoginResponse
import com.example.starsbar_app.models.RegisterRequest
import com.example.starsbar_app.models.Restaurant
import com.example.starsbar_app.models.Review
import com.example.starsbar_app.models.UserIdResponse
import com.example.starsbar_app.models.UserNameResponse
import com.example.starsbar_app.network.ApiService

class UserController {

    private val apiService = ApiService.create()

    suspend fun loginUser(mail: String, pass: String): LoginResponse {
        val request = LoginRequest(mail, pass)
        return apiService.login(request)
    }

    suspend fun getUserName(id: Int): UserNameResponse {
        return apiService.getUserName(id)
    }

    suspend fun getUserId(mail: String): UserIdResponse {
        return apiService.getUserId(mail)
    }

    suspend fun registerUser(name: String, lastname: String, location: String, mail: String, pass: String, rol: String): ApiResponse {
        val request = RegisterRequest(name, lastname, location, mail, pass, rol)
        return apiService.registerUser(request)
    }
}
