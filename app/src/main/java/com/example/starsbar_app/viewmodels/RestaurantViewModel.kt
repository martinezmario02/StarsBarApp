package com.example.starsbar_app.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.starsbar_app.controllers.RestaurantController
import com.example.starsbar_app.models.Restaurant
import kotlinx.coroutines.launch

class RestaurantViewModel : ViewModel() {
    private val restaurantController = RestaurantController()
    val restaurants = mutableStateOf<List<Restaurant>>(emptyList())

    fun fetchRestaurants() {
        viewModelScope.launch {
            restaurants.value = try {
                restaurantController.getRestaurants()
            } catch (e: Exception) {
                emptyList()
            }
        }
    }
}
