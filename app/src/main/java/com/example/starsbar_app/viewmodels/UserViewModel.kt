package com.example.starsbar_app.viewmodels

import android.util.Log
import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.starsbar_app.controllers.UserController
import kotlinx.coroutines.launch
import retrofit2.HttpException

class UserViewModel : ViewModel() {
    private val userController = UserController()
    var userNames = mutableStateMapOf<Int, String>()
        private set

    fun fetchUserName(id: Int) {
        Log.d("UserViewModel", "ID: $id")
        if (id <= 0 || userNames.containsKey(id)) return

        viewModelScope.launch {
            val name = userController.getUserName(id).name
            userNames[id] = name
        }
    }



}

