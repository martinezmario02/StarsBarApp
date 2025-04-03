package com.example.starsbar_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import com.example.starsbar_app.views.LoginScreen
import com.example.starsbar_app.views.MainLayout
import com.example.starsbar_app.views.RestaurantListScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StarsBarTheme {
                val navController = rememberNavController()
                AppNavHost(navController)
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

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        // Login screen doesn't use the MainLayout
        composable("login") {
            LoginScreen(navController = navController)
        }

        // Main screen uses MainLayout
        composable("main") {
            MainScreen(navController = navController)
        }

        // Restaurant list screen uses MainLayout
        composable("restaurant_list") {
            val restaurantViewModel: RestaurantViewModel = viewModel()

            MainLayout(
                navController = navController,
                title = "Restaurantes"
            ) {
                RestaurantListScreen(viewModel = restaurantViewModel)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    StarsBarTheme {
        val navController = rememberNavController()
        AppNavHost(navController)
    }
}