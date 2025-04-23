package com.example.starsbar_app.views

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import com.example.starsbar_app.ui.theme.NavyBlue
import com.example.starsbar_app.viewmodels.RestaurantViewModel
import com.example.starsbar_app.viewmodels.UserViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import androidx.compose.ui.graphics.Brush
import coil.compose.rememberAsyncImagePainter
import com.example.starsbar_app.R
import java.io.File

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RestaurantDetailsScreen(viewModel: RestaurantViewModel, userViewModel: UserViewModel, restaurant: Restaurant) {
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
        ReviewsSection(viewModel, userViewModel, restaurant, reviews)
    }
}

@Composable
fun HeaderImage(restaurant: Restaurant, reviewsSize: Int) {
    val context = LocalContext.current
    val imageFile = restaurant.image?.let { File(context.filesDir, it) }
    val painter = if (imageFile?.exists() == true) {
        rememberAsyncImagePainter(model = imageFile)
    } else {
        painterResource(id = R.drawable.defecto)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
    ) {
        Image(
            painter = painter,
            contentDescription = "Restaurant Image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
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
                    color = Color(0xFFFFA000),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Row(modifier = Modifier.padding(start = 4.dp)) {
                    repeat(5) { index ->
                        val starColor =
                            if (index < restaurant.average_rating.toInt()) Color(0xFFFFA000)
                            else Color.Gray.copy(alpha = 0.5f)
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
@Composable
fun ReviewsSection(viewModel: RestaurantViewModel, userViewModel: UserViewModel, restaurant: Restaurant, reviews: List<Review>) {
    var showReviewDialog by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

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

        RatingSummaryCard(restaurant, reviews)

        OutlinedButton(
            onClick = { showReviewDialog = true },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .height(56.dp),
            shape = RoundedCornerShape(28.dp),
            border = ButtonDefaults.outlinedButtonBorder
        ) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Escribir una opinión",
                fontSize = 16.sp
            )
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

                    LaunchedEffect(review.user_id) {
                        userViewModel.fetchUserName(review.user_id)
                    }

                    val name = userViewModel.userNames[review.user_id] ?: ""

                    ReviewItem(
                        name = name,
                        rating = review.rating.toInt(),
                        comment = review.comment,
                        date = formattedDate
                    )

                }
            }
        }
    }

    if (showReviewDialog) {
        var rating by remember { mutableStateOf(5) }
        var comment by remember { mutableStateOf("") }
        var isSubmitting by remember { mutableStateOf(false) }

        AlertDialog(
            onDismissRequest = { showReviewDialog = false },
            title = { Text("Escribe tu opinión") },
            text = {
                Column {
                    Text("Calificación:")
                    Row(
                        modifier = Modifier.padding(vertical = 8.dp)
                    ) {
                        repeat(5) { index ->
                            IconButton(
                                onClick = { rating = index + 1 }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = "Star ${index + 1}",
                                    tint = if (index < rating) Yellow else Color.Gray.copy(alpha = 0.5f),
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                        }
                    }

                    OutlinedTextField(
                        value = comment,
                        onValueChange = { comment = it },
                        label = { Text("Comentario") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        maxLines = 5
                    )

                    if (isSubmitting) {
                        LinearProgressIndicator(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp)
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (userViewModel.currentUserId == null) {
                            Log.e("AddReview", "No se puede añadir la review: userId es null")
                            return@Button
                        }

                        isSubmitting = true
                        scope.launch {
                            try {
                                viewModel.addReview(
                                    restaurantId = restaurant.id,
                                    userId = userViewModel.currentUserId!!,
                                    rating = rating,
                                    comment = comment
                                )
                                isSubmitting = false
                                showReviewDialog = false
                                viewModel.fetchReviews(restaurant.id)
                            } catch (e: Exception) {
                                Log.e("AddReview", "Error al añadir review: ${e.message}", e)
                                isSubmitting = false
                            }
                        }
                    },
                    enabled = !isSubmitting && comment.isNotBlank()
                ) {
                    Text("Enviar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showReviewDialog = false },
                    enabled = !isSubmitting
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
fun RatingSummaryCard(restaurant: Restaurant, reviews: List<Review>) {
    val excellentCount = reviews.count { it.rating >= 4.5f }
    val goodCount = reviews.count { it.rating >= 3.5f && it.rating < 4.5f }
    val normalCount = reviews.count { it.rating >= 2.5f && it.rating < 3.5f }
    val badCount = reviews.count { it.rating >= 1.5f && it.rating < 2.5f }
    val terribleCount = reviews.count { it.rating < 1.5f }

    val totalReviews = reviews.size.coerceAtLeast(1)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.LightGray
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(0.5f)
                ) {
                    Text(
                        text = String.format("%.1f", restaurant.average_rating),
                        fontSize = 46.sp,
                        fontWeight = FontWeight.Bold,
                        color = NavyBlue
                    )
                    Text(
                        text = "Muy bueno",
                        fontSize = 18.sp,
                        color = NavyBlue
                    )
                    Row(
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        repeat(5) { index ->
                            val isFilled = index < restaurant.average_rating.toInt()
                            val isHalfFilled = !isFilled && index == restaurant.average_rating.toInt() &&
                                    restaurant.average_rating - restaurant.average_rating.toInt() >= 0.5f

                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = if (isFilled || isHalfFilled) Yellow else Color.Gray.copy(alpha = 0.5f),
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                    Text(
                        text = totalReviews.toString() + " reseña/s",
                        fontSize = 16.sp,
                        color = NavyBlue,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                Column(
                    modifier = Modifier.weight(0.7f)
                ) {
                    RatingBar(label = "Excelente", count = excellentCount, percentage = excellentCount.toFloat() / totalReviews)
                    RatingBar(label = "Bueno", count = goodCount, percentage = goodCount.toFloat() / totalReviews)
                    RatingBar(label = "Normal", count = normalCount, percentage = normalCount.toFloat() / totalReviews)
                    RatingBar(label = "Malo", count = badCount, percentage = badCount.toFloat() / totalReviews)
                    RatingBar(label = "Pésimo", count = terribleCount, percentage = terribleCount.toFloat() / totalReviews)
                }
            }
        }
    }
}

@Composable
fun RatingBar(label: String, count: Int, percentage: Float) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            color = NavyBlue,
            modifier = Modifier.width(80.dp)
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(Color.Gray.copy(alpha = 0.3f))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(percentage)
                    .clip(RoundedCornerShape(4.dp))
                    .background(NavyBlue)
            )
        }

        Text(
            text = count.toString(),
            fontSize = 14.sp,
            color = NavyBlue,
            modifier = Modifier.width(40.dp),
            textAlign = TextAlign.End
        )
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
                        text = if (name.isNotEmpty()) name.first().toString() else "?",
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
        phone = "676767676",
        pass = "topsecret"
    )

    val dummyUserViewModel = UserViewModel()
    val dummyViewModel = RestaurantViewModel()

    StarsBarTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            RestaurantDetailsScreen(
                viewModel = dummyViewModel,
                userViewModel = dummyUserViewModel,
                restaurant = sampleRestaurant
            )
        }
    }
}
