package com.example.starsbar_app.controllers

import com.example.starsbar_app.models.ApiResponse
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
}
