package com.example.starsbar_app.network

import com.example.starsbar_app.models.LoginRequest
import com.example.starsbar_app.models.LoginResponse
import com.example.starsbar_app.models.Restaurant
import com.example.starsbar_app.models.Review
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Path

interface ApiService {

    @POST("api/users/login") // Ruta para login
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @GET("api/restaurants") // Ruta para obtener restaurantes
    suspend fun getRestaurants(): List<Restaurant>

    @GET("api/restaurants/{id}") // Ruta para obtener la información del restaurante
    suspend fun getRestaurantById(@Path("id") id: Int): Restaurant

    @GET("api/restaurants/{id}/reviews") // Ruta para obtener las valoraciones del restaurante
    suspend fun getRestaurantReviews(@Path("id") id: Int): List<Review>

    companion object {
        private const val BASE_URL = "http://10.0.2.2:3000/"

        fun create(): ApiService {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService::class.java)
        }
    }
}
