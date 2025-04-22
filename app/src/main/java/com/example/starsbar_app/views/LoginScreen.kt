package com.example.starsbar_app.views

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.starsbar_app.AppNavHost
import com.example.starsbar_app.ui.theme.*
import com.example.starsbar_app.viewmodels.RestaurantViewModel
import com.example.starsbar_app.viewmodels.UserViewModel

@Composable
fun LoginScreen(navController: NavController, userViewModel: UserViewModel = viewModel()) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(ColorPrimaryDark, ColorPrimary)
                )
            )
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "StarsBar",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = White
            )

            Text(
                text = "Comer bien, nunca fue tan fácil",
                fontSize = 16.sp,
                color = White,
                modifier = Modifier.padding(top = 8.dp)
            )

            Row(
                modifier = Modifier.padding(top = 8.dp, bottom = 20.dp)
            ) {
                repeat(5) {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = "Star",
                        tint = Yellow,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                placeholder = { Text("Correo electrónico", color = LightGray) },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = White,
                    unfocusedTextColor = White,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedBorderColor = White,
                    unfocusedBorderColor = LightGray,
                    focusedPlaceholderColor = LightGray,
                    unfocusedPlaceholderColor = LightGray
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                placeholder = { Text("Contraseña", color = LightGray) },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = White,
                    unfocusedTextColor = White,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedBorderColor = White,
                    unfocusedBorderColor = LightGray,
                    focusedPlaceholderColor = LightGray,
                    unfocusedPlaceholderColor = LightGray
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    if (email.isEmpty() || password.isEmpty()) {
                        showError = true
                        errorMessage = "Por favor, complete todos los campos"
                    } else {
                        showError = false
                        userViewModel.login(email, password) { success, result ->
                            if (success) {
                                navController.navigate("restaurant_list")
                            } else {
                                showError = true
                                errorMessage = "Error al introducir las credenciales"
                            }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ButtonBlue),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(text = "Iniciar Sesión", color = White, fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "¿Aún no te has registrado?",
                    color = LightGray,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.width(4.dp))
                TextButton(onClick = { navController.navigate("register_screen") }) {
                    Text(
                        text = "Regístrate aquí",
                        color = Yellow,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            if (showError && errorMessage != null) {
                Text(
                    text = errorMessage ?: "",
                    color = Color.Red,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 10.dp)
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
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