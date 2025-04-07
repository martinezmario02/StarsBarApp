package com.example.starsbar_app.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import com.example.starsbar_app.models.Restaurant
import com.example.starsbar_app.viewmodels.RestaurantViewModel
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.layout.ContentScale
import androidx.navigation.NavHostController

@Composable
fun RestaurantListScreen(viewModel: RestaurantViewModel, navController: NavHostController) {
    val restaurants by viewModel.restaurants
    val isLoading = remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        isLoading.value = true
        viewModel.fetchRestaurants()
        isLoading.value = false
    }

    if (isLoading.value) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else if (restaurants.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("No hay restaurantes disponibles.")
        }
    } else {
        LazyColumn(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)) {
            items(restaurants) { restaurant ->
                RestaurantItem(restaurant) {
                    navController.navigate("restaurant_details/${restaurant.id}")
                }
            }
        }
    }
}

@Composable
fun RestaurantItem(restaurant: Restaurant, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() }
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            val imageResId = restaurant.image?.let { imageName ->
                val cleanName = imageName.substringBeforeLast(".").lowercase()
                val resId = LocalContext.current.resources.getIdentifier(
                    cleanName,
                    "drawable",
                    LocalContext.current.packageName
                )
                if (resId != 0) resId else null
            }

            imageResId?.let {
                Image(
                    painter = painterResource(id = it),
                    contentDescription = "Restaurant Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    contentScale = ContentScale.Crop
                )
            }

            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                Text(
                    text = restaurant.name,
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(
                    text = restaurant.location,
                    color = Color.Gray,
                    fontSize = 14.sp
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = restaurant.description,
                        color = Color.Gray,
                        fontSize = 12.sp,
                        maxLines = 1,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = "⭐ ${restaurant.average_rating}",
                        color = Color(0xFFFFA000),
                        fontSize = 20.sp
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewRestaurantListScreen() {
    val mockRestaurants = listOf(
        Restaurant(
            id = 1,
            name = "Restaurante Ejemplo",
            location = "Ubicación Ejemplo",
            description = "Descripción del restaurante ejemplo",
            average_rating = 4.5f,
            image = "peruano.jpg",
            mail = "a@a.a",
            phone = "678909090"
        ),
        Restaurant(
            id = 2,
            name = "Restaurante Prueba",
            location = "Ubicación Prueba",
            description = "Descripción del restaurante prueba",
            average_rating = 4.0f,
            image = "peruano.jpg",
            mail = "a@a.a",
            phone = "678909090"
        )
    )

    RestaurantListScreenContent(restaurants = mockRestaurants)
}

@Composable
fun RestaurantListScreenContent(restaurants: List<Restaurant>) {
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        items(restaurants) { restaurant ->
            RestaurantItem(restaurant, {})
        }
    }
}