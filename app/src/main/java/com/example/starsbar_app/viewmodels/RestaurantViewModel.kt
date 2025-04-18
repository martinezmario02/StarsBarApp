package com.example.starsbar_app.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.starsbar_app.controllers.RestaurantController
import com.example.starsbar_app.models.Restaurant
import com.example.starsbar_app.models.Review
import com.example.starsbar_app.models.ReviewRequest
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class RestaurantViewModel : ViewModel() {
    private val restaurantController = RestaurantController()
    val restaurants = mutableStateOf<List<Restaurant>>(emptyList())
    val reviews = mutableStateOf<List<Review>>(emptyList())
    var reviewName = ""

    fun fetchRestaurants() {
        viewModelScope.launch {
            restaurants.value = try {
                restaurantController.getRestaurants()
            } catch (e: Exception) {
                emptyList()
            }
        }
    }

    fun fetchReviews(id: Int){
        viewModelScope.launch {
            reviews.value = try {
                restaurantController.getRestaurantReviews(id)
            } catch (e: Exception) {
                emptyList()
            }
        }
    }

    fun addReview(restaurantId: Int, userId: Int, rating: Int, comment: String) {
        viewModelScope.launch {
            delay(1000)

            val newReview = ReviewRequest(
                user_id = userId,
                rating = rating.toFloat(),
                comment = comment
            )

            restaurantController.addReview(restaurantId, newReview)
        }
    }
}
