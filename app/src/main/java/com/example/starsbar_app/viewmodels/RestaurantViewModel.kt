package com.example.starsbar_app.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.starsbar_app.controllers.RestaurantController
import com.example.starsbar_app.models.Restaurant
import com.example.starsbar_app.models.Review
import kotlinx.coroutines.launch

class RestaurantViewModel : ViewModel() {
    private val restaurantController = RestaurantController()
    val restaurants = mutableStateOf<List<Restaurant>>(emptyList())
    val reviews = mutableStateOf<List<Review>>(emptyList())

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
}
