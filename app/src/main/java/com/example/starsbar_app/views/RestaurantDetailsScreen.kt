package com.example.starsbar_app.views

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.starsbar_app.models.Restaurant
import com.example.starsbar_app.ui.theme.Yellow
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.starsbar_app.ui.theme.StarsBarTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.example.starsbar_app.models.Review
import com.example.starsbar_app.viewmodels.RestaurantViewModel
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RestaurantDetailsScreen(viewModel: RestaurantViewModel, navController: NavController, restaurant: Restaurant) {
    val scrollState = rememberScrollState()
    val reviews by viewModel.reviews
    val isLoading = remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        isLoading.value = true
        viewModel.fetchReviews(restaurant.id)
        isLoading.value = false
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        HeaderImage(restaurant, reviews.size)
        DescriptionSection(restaurant)
        //LocationSection(restaurant)
        ContactInfoSection(restaurant)
        ReviewsSection(reviews)
    }
}

@Composable
fun HeaderImage(restaurant: Restaurant, reviewsSize: Int) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
    ) {
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
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } ?: Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.LightGray)
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    androidx.compose.ui.graphics.Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.7f)
                        ),
                        startY = 300f
                    )
                )
        )

        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
        ) {
            Text(
                text = restaurant.name,
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 4.dp)
            ) {
                Text(
                    text = "${restaurant.average_rating}",
                    color = Yellow,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Row(
                    modifier = Modifier.padding(start = 4.dp)
                ) {
                    repeat(5) { index ->
                        val starColor =
                            if (index < restaurant.average_rating.toInt()) Yellow else Color.Gray.copy(
                                alpha = 0.5f
                            )
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = starColor,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Text(
                    text = "(${reviewsSize} reseñas)",
                    color = Color.White,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = restaurant.location,
                    color = Color.White,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }
        }
    }
}

@Composable
fun DescriptionSection(restaurant: Restaurant) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        SectionTitle(title = "Acerca de")

        Text(
            text = restaurant.description,
            fontSize = 16.sp,
            lineHeight = 24.sp,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun formatDateTime(dateTimeString: String): String {
    return try {
        val dateTime = LocalDateTime.parse(dateTimeString)
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
        dateTime.format(formatter)
    } catch (e: Exception) {
        dateTimeString
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ReviewsSection(reviews: List<Review>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            SectionTitle(title = "Reseñas", modifier = Modifier.weight(1f))
        }

        if (reviews.isEmpty()) {
            Text(
                text = "¡Sé el primero en comentar!",
                fontSize = 16.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                reviews.forEach { review ->
                    val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                    val formattedDate = formatter.format(review.created_at)

                    ReviewItem(
                        name = review.name,
                        rating = review.rating.toInt(),
                        comment = review.comment,
                        date = formattedDate
                    )
                }
            }
        }
    }
}

@Composable
fun ReviewItem(name: String, rating: Int, comment: String, date: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = name.first().toString(),
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 12.dp)
                ) {
                    Text(
                        text = name,
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        repeat(5) { index ->
                            val starColor = if (index < rating) Yellow else Color.Gray.copy(alpha = 0.3f)
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = starColor,
                                modifier = Modifier.size(16.dp)
                            )
                        }

                        Text(
                            text = date,
                            fontSize = 12.sp,
                            color = Color.Gray,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }
            }

            Text(
                text = comment,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
fun LocationSection(restaurant: Restaurant) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        SectionTitle(title = "Ubicación")

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .padding(top = 8.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    modifier = Modifier.size(48.dp),
                    tint = Color.White
                )
            }
        }

        Text(
            text = restaurant.location,
            fontSize = 14.sp,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

@Composable
fun ContactInfoSection(restaurant: Restaurant) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        SectionTitle(title = "Información de Contacto")

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Phone,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )

            Text(
                text = restaurant.phone,
                fontSize = 16.sp,
                modifier = Modifier.padding(start = 12.dp)
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Email,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )

            Text(
                text = restaurant.mail,
                fontSize = 16.sp,
                modifier = Modifier.padding(start = 12.dp)
            )
        }
    }
}

@Composable
fun SectionTitle(title: String, modifier: Modifier = Modifier) {
    Text(
        text = title,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        modifier = modifier
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, heightDp = 1200)
@Composable
fun RestaurantDetailsScreenPreview() {
    val sampleRestaurant = Restaurant(
        id = 1,
        name = "La Parrilla Española",
        location = "Calle Mayor 23, Madrid",
        description = "Restaurante especializado en carnes a la parrilla con productos de primera calidad. " +
                "Nuestros platos están elaborados con ingredientes frescos y locales, seleccionados diariamente " +
                "para garantizar la mejor experiencia gastronómica.",
        image = "peruano",
        average_rating = 4.7f,
        mail ="laparrillada@gmail.com",
        phone = "676767676"
    )

    val dummyNavController = rememberNavController()
    val dummyViewModel = RestaurantViewModel()

    StarsBarTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            RestaurantDetailsScreen(
                viewModel = dummyViewModel,
                navController = dummyNavController,
                restaurant = sampleRestaurant
            )
        }
    }
}

