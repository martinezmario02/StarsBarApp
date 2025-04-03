package com.example.starsbar_app.controllers

import com.example.starsbar_app.models.Restaurant
import com.example.starsbar_app.network.ApiService

class RestaurantController {

    private val apiService = ApiService.create()

    suspend fun getRestaurants(): List<Restaurant> {
        return apiService.getRestaurants()
    }

    suspend fun getRestaurantById(id: Int): Restaurant{
        return apiService.getRestaurantById(id)
    }
}
