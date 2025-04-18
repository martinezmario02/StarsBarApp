package com.example.starsbar_app.network

import com.example.starsbar_app.models.ApiResponse
import com.example.starsbar_app.models.LoginRequest
import com.example.starsbar_app.models.LoginResponse
import com.example.starsbar_app.models.Restaurant
import com.example.starsbar_app.models.Review
import com.example.starsbar_app.models.ReviewRequest
import com.example.starsbar_app.models.UserIdResponse
import com.example.starsbar_app.models.UserNameResponse
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

    @GET("api/users/{user_id}/name") // Ruta para obtener el nombre del usuario
    suspend fun getUserName(@Path("user_id") id: Int): UserNameResponse

    @GET("/api/users/{mail}/id")  // Ruta para obtener el id del usuario
    suspend fun getUserId(@Path("mail") email: String): UserIdResponse

    @POST("api/restaurants/{id}/reviews") // Ruta para añadir una nueva valoración
    suspend fun addReview(@Path("id") restaurantId: Int, @Body review: ReviewRequest): ApiResponse


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
