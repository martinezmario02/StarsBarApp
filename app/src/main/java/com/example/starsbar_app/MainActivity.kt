package com.example.starsbar_app

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.example.starsbar_app.ui.theme.StarsBarTheme
import com.example.starsbar_app.viewmodels.RestaurantViewModel
import com.example.starsbar_app.viewmodels.UserViewModel
import com.example.starsbar_app.views.LoginScreen
import com.example.starsbar_app.views.MainLayout
import com.example.starsbar_app.views.RestaurantDetailsScreen
import com.example.starsbar_app.views.RestaurantListScreen

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StarsBarTheme {
                val navController = rememberNavController()
                val restaurantViewModel: RestaurantViewModel = viewModel()
                val userViewModel: UserViewModel = viewModel()
                AppNavHost(navController, restaurantViewModel, userViewModel)
            }
        }
    }
}

@Composable
fun MainScreen(navController: NavHostController) {
    MainLayout(
        navController = navController,
        showBackButton = false
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Bienvenido a StarsBar", fontSize = 24.sp, modifier = Modifier.padding(bottom = 16.dp))
            Button(onClick = { navController.navigate("restaurant_list") }) {
                Text("Ir a la lista de Restaurantes")
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavHost(navController: NavHostController, restaurantViewModel: RestaurantViewModel, userViewModel: UserViewModel) {
    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        // Login
        composable("login") {
            LoginScreen(navController = navController, userViewModel)
        }

        // Main
        composable("main") {
            MainScreen(navController = navController)
        }

        // Lista de restaurantes
        composable("restaurant_list") {
            MainLayout(
                navController = navController,
                title = "Restaurantes"
            ) {
                RestaurantListScreen(viewModel = restaurantViewModel, userViewModel = userViewModel, navController = navController)
            }
        }

        // Información del restaurante
        composable("restaurant_details/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")?.toIntOrNull()
            Log.d("NAVDEBUG", "ID recibido: $id")

            val restaurant = restaurantViewModel.restaurants.value.find { it.id == id }
            Log.d("NAVDEBUG", "Restaurante: $restaurant")

            if (restaurant != null) {
                MainLayout(navController = navController, title = restaurant.name) {
                    RestaurantDetailsScreen(viewModel = restaurantViewModel, userViewModel = userViewModel, restaurant = restaurant)
                }
            } else {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Restaurante no encontrado.")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    StarsBarTheme {
        val navController = rememberNavController()
        val restaurantViewModel: RestaurantViewModel = viewModel()
        val userViewModel: UserViewModel = viewModel()
        AppNavHost(navController, restaurantViewModel, userViewModel)
    }
}