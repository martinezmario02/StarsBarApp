package com.example.starsbar_app.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import com.example.starsbar_app.viewmodels.UserViewModel
import com.example.starsbar_app.R
import coil.compose.rememberAsyncImagePainter
import java.io.File
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.text.input.TextFieldValue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RestaurantListScreen(
    viewModel: RestaurantViewModel,
    userViewModel: UserViewModel,
    navController: NavHostController
) {
    val restaurants by viewModel.restaurants
    val isLoading = remember { mutableStateOf(true) }
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }

    LaunchedEffect(Unit) {
        isLoading.value = true
        viewModel.fetchRestaurants()
        isLoading.value = false
    }

    val filteredRestaurants = restaurants.filter {
        it.name.contains(searchQuery.text, ignoreCase = true)
    }

    if (isLoading.value) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        Column(modifier = Modifier.fillMaxSize()) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Buscar restaurante") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Buscar",
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary
                )
            )

            if (filteredRestaurants.isEmpty()) {
                Box(
                    modifier = Modifier.weight(1f).fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No se encontraron restaurantes.")
                }
            } else {
                LazyColumn(modifier = Modifier
                    .weight(1f)
                    .padding(16.dp)) {
                    items(filteredRestaurants) { restaurant ->
                        RestaurantItem(restaurant) {
                            navController.navigate("restaurant_details/${restaurant.id}")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RestaurantItem(restaurant: Restaurant, onClick: () -> Unit) {
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() }
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            val fileImage = restaurant.image?.let { File(context.filesDir, it) }

            val painter = when {
                fileImage?.exists() == true -> {
                    rememberAsyncImagePainter(model = fileImage)
                }
                else -> {
                    val resourceId = context.resources.getIdentifier(
                        restaurant.image?.substringBefore(".") ?: "",
                        "drawable",
                        context.packageName
                    )
                    if (resourceId != 0) {
                        painterResource(id = resourceId)
                    } else {
                        painterResource(id = R.drawable.defecto)
                    }
                }
            }

            Image(
                painter = painter,
                contentDescription = "Restaurant Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                contentScale = ContentScale.Crop
            )

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
            phone = "678909090",
            pass = "topsecret"
        ),
        Restaurant(
            id = 2,
            name = "Restaurante Prueba",
            location = "Ubicación Prueba",
            description = "Descripción del restaurante prueba",
            average_rating = 4.0f,
            image = "peruano.jpg",
            mail = "a@a.a",
            phone = "678909090",
            pass = "topsecret"
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