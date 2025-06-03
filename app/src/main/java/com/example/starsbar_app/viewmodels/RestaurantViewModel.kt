package com.example.starsbar_app.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.starsbar_app.controllers.RestaurantController
import com.example.starsbar_app.models.PositiveReviewResponse
import com.example.starsbar_app.models.Restaurant
import com.example.starsbar_app.models.Review
import com.example.starsbar_app.models.ReviewRequest
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class RestaurantViewModel : ViewModel() {
    private val restaurantController = RestaurantController()
    val restaurants = mutableStateOf<List<Restaurant>>(emptyList())
    val reviews = mutableStateOf<List<Review>>(emptyList())
    var positiveReviews: PositiveReviewResponse? by mutableStateOf(null)
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

            positiveReviews = try {
                restaurantController.getPositiveReviews(id)
            } catch (e: Exception) {
                null
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

    fun registerRestaurant(name: String, location: String, description: String, mail: String, phone: String, image: String, pass: String, menu: String, onResult: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            try {
                val response = restaurantController.registerRestaurant(name, location, description, mail, phone, image, pass, menu)
                if (response.success) {
                    onResult(true, response.message)
                } else {
                    onResult(false, response.message)
                }
            } catch (e: Exception) {
                Log.e("REGISTERRESTAUTANT", "Error: ${e.message}", e)
                onResult(false, "Error: ${e.message}")
            }
        }
    }
}
