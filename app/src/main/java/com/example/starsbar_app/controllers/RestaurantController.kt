package com.example.starsbar_app.controllers

import com.example.starsbar_app.models.ApiResponse
import com.example.starsbar_app.models.RegisterRequest
import com.example.starsbar_app.models.Restaurant
import com.example.starsbar_app.models.Review
import com.example.starsbar_app.models.ReviewRequest
import com.example.starsbar_app.network.ApiService

class RestaurantController {

    private val apiService = ApiService.create()

    suspend fun getRestaurants(): List<Restaurant> {
        return apiService.getRestaurants()
    }

    suspend fun getRestaurantById(id: Int): Restaurant{
        return apiService.getRestaurantById(id)
    }

    suspend fun getRestaurantReviews(id: Int): List<Review> {
        return apiService.getRestaurantReviews(id)
    }

    suspend fun addReview(restaurantId: Int, review: ReviewRequest): ApiResponse {
        return apiService.addReview(restaurantId, review)
    }

    suspend fun registerRestaurant(name: String, location: String, description: String, mail: String, phone: String, image: String, pass: String, menu: String): ApiResponse {
        val request = Restaurant(-1, name, location, description, 0.0f, mail, phone, image, pass, menu)
        return apiService.registerRestaurant(request)
    }
}
