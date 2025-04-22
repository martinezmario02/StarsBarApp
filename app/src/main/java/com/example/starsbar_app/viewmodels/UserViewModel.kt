package com.example.starsbar_app.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.starsbar_app.controllers.UserController
import com.auth0.android.jwt.JWT
import kotlinx.coroutines.launch

class UserViewModel : ViewModel() {
    private val userController = UserController()
    var userNames = mutableStateMapOf<Int, String>()
    var currentUserId by mutableStateOf<Int?>(null)
    var currentUserToken by mutableStateOf<String?>(null)

    fun login(mail: String, pass: String, onResult: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            try {
                val response = userController.loginUser(mail, pass)
                if (response.success && response.token != null) {
                    currentUserToken = response.token

                    val jwt = JWT(response.token)
                    val idFromToken = jwt.getClaim("id").asInt()
                    currentUserId = idFromToken
                    Log.d("LOGIN", "User ID obtenido del token: $idFromToken")

                    onResult(true, null)
                } else {
                    onResult(false, response.message)
                }
            } catch (e: Exception) {
                Log.e("LOGIN", "Error: ${e.message}")
                onResult(false, e.message)
            }
        }
    }

    fun fetchUserName(id: Int) {
        if (id <= 0 || userNames.containsKey(id)) return

        viewModelScope.launch {
            val name = userController.getUserName(id).name
            userNames[id] = name
        }
    }

    fun register(name: String, lastname: String, location: String, mail: String, pass: String, rol: String, onResult: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            try {
                val response = userController.registerUser(name, lastname, location, mail, pass, rol)
                if (response.success) {
                    onResult(true, response.message)
                } else {
                    onResult(false, response.message)
                }
            } catch (e: Exception) {
                Log.e("REGISTER", "Error: ${e.message}", e)
                onResult(false, "Error: ${e.message}")
            }
        }
    }
}