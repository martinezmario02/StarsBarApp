package com.example.starsbar_app.views

import android.content.Context
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.starsbar_app.models.RegisterRequest
import com.example.starsbar_app.ui.theme.*
import com.example.starsbar_app.viewmodels.RestaurantViewModel
import com.example.starsbar_app.viewmodels.UserViewModel
import java.io.File
import java.io.FileOutputStream
import androidx.core.net.toUri

@Composable
fun RegisterScreen(navController: NavController, userViewModel: UserViewModel = viewModel(), restaurantViewModel: RestaurantViewModel = viewModel()) {
    var selectedTabIndex by remember { mutableStateOf(0) }
    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(ColorPrimaryDark, ColorPrimary)
                )
            )
            .padding(24.dp)
    ) {
        IconButton(
            onClick = { navController.navigateUp() },
            modifier = Modifier.align(Alignment.TopStart)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = White
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState)
                .padding(top = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Únete a nosotros",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = White
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

            TabRow(
                selectedTabIndex = selectedTabIndex,
                containerColor = Color.Transparent,
                contentColor = White,
                divider = { Divider(color = LightGray.copy(alpha = 0.3f)) }
            ) {
                Tab(
                    selected = selectedTabIndex == 0,
                    onClick = { selectedTabIndex = 0 },
                    text = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "Usuario", fontSize = 14.sp)
                        }
                    },
                    selectedContentColor = Yellow,
                    unselectedContentColor = White.copy(alpha = 0.7f)
                )

                Tab(
                    selected = selectedTabIndex == 1,
                    onClick = { selectedTabIndex = 1 },
                    text = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.ShoppingCart,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "Restaurante", fontSize = 14.sp)
                        }
                    },
                    selectedContentColor = Yellow,
                    unselectedContentColor = White.copy(alpha = 0.7f)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            when (selectedTabIndex) {
                0 -> UserRegistrationForm(navController, userViewModel)
                1 -> RestaurantRegistrationForm(navController, userViewModel, restaurantViewModel)
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

@Composable
fun UserRegistrationForm(navController: NavController, userViewModel: UserViewModel) {
    var name by remember { mutableStateOf("") }
    var lastname by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var mail by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            placeholder = { Text("Nombre", color = LightGray) },
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
            singleLine = true,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = LightGray
                )
            }
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = lastname,
            onValueChange = { lastname = it },
            placeholder = { Text("Apellido", color = LightGray) },
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
            singleLine = true,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = LightGray
                )
            }
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = location,
            onValueChange = { location = it },
            placeholder = { Text("Ciudad", color = LightGray) },
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
            singleLine = true,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = LightGray
                )
            }
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = mail,
            onValueChange = { mail = it },
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
            singleLine = true,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = null,
                    tint = LightGray
                )
            }
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
            singleLine = true,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null,
                    tint = LightGray
                )
            }
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            placeholder = { Text("Confirmar contraseña", color = LightGray) },
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
            singleLine = true,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null,
                    tint = LightGray
                )
            }
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                if (name.isEmpty() || lastname.isEmpty() || location.isEmpty() || mail.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                    showError = true
                    errorMessage = "Por favor, complete todos los campos"
                } else if (password != confirmPassword) {
                    showError = true
                    errorMessage = "Las contraseñas no coinciden"
                } else {
                    showError = false
                    userViewModel.register(name, lastname, location, mail, password, "user") { success, result ->
                        if (success) {
                            navController.navigate("login")
                        } else {
                            showError = true
                            errorMessage = "Error al registrar el usuario"
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
            Text(text = "Registrarse", color = White, fontSize = 16.sp)
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

fun saveImageToInternalStorage(context: Context, uri: Uri, restaurantName: String): String? {
    return try {
        val inputStream = context.contentResolver.openInputStream(uri)
        val sanitizedName = restaurantName.lowercase().replace("\\s+".toRegex(), "_")
        val fileName = "$sanitizedName.jpg"
        val file = File(context.filesDir, fileName)
        val outputStream = FileOutputStream(file)
        inputStream?.copyTo(outputStream)
        inputStream?.close()
        outputStream.close()
        fileName
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

@Composable
fun RestaurantRegistrationForm(navController: NavController, userViewModel: UserViewModel, restaurantViewModel: RestaurantViewModel) {
    var name by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var mail by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.Gray.copy(alpha = 0.3f))
                .border(
                    width = 1.dp,
                    color = LightGray,
                    shape = RoundedCornerShape(8.dp)
                )
                .clickable { launcher.launch("image/*") },
            contentAlignment = Alignment.Center
        ) {
            if (imageUri != null) {
                val imageFile = imageUri?.let { uri ->
                    val nameForPreview = name.ifEmpty { "temp" }
                    saveImageToInternalStorage(context, uri, nameForPreview)
                }?.let { File(context.filesDir, it) }

                if (imageFile != null && imageFile.exists()) {
                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(imageFile.toUri())
                            .crossfade(true)
                            .build(),
                        contentDescription = "Restaurant Image",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            } else {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.AccountBox,
                        contentDescription = "Add Photo",
                        tint = White,
                        modifier = Modifier.size(32.dp)
                    )
                    Text(
                        text = "Añadir imagen",
                        color = White,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            placeholder = { Text("Nombre del restaurante", color = LightGray) },
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
            singleLine = true,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = null,
                    tint = LightGray
                )
            }
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = location,
            onValueChange = { location = it },
            placeholder = { Text("Dirección", color = LightGray) },
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
            singleLine = true,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = LightGray
                )
            }
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            placeholder = { Text("Descripción", color = LightGray) },
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
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
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = null,
                    tint = LightGray
                )
            }
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            placeholder = { Text("Teléfono", color = LightGray) },
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
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            singleLine = true,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Phone,
                    contentDescription = null,
                    tint = LightGray
                )
            }
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = mail,
            onValueChange = { mail = it },
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
            singleLine = true,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = null,
                    tint = LightGray
                )
            }
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
            singleLine = true,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null,
                    tint = LightGray
                )
            }
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            placeholder = { Text("Confirmar contraseña", color = LightGray) },
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
            singleLine = true,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null,
                    tint = LightGray
                )
            }
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                if (name.isEmpty() || location.isEmpty() || description.isEmpty() || phone.isEmpty() || mail.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                    showError = true
                    errorMessage = "Por favor, complete todos los campos"
                } else if (password != confirmPassword) {
                    showError = true
                    errorMessage = "Las contraseñas no coinciden"
                } else if (imageUri == null) {
                    showError = true
                    errorMessage = "Por favor, selecciona una imagen"
                } else {
                    showError = false
                    val imageName = saveImageToInternalStorage(context, imageUri!!, name)
                    if (imageName == null) {
                        showError = true
                        errorMessage = "No se pudo guardar la imagen"
                    }
                    else {
                        restaurantViewModel.registerRestaurant(
                            name, location, description, mail, phone, imageName, password
                        ) { success, result ->
                            if (success) {
                                navController.navigate("login")
                            } else {
                                showError = true
                                errorMessage = result ?: "Error al registrar el restaurante"
                            }
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
            Text(text = "Registrar restaurante", color = White, fontSize = 16.sp)
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

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    StarsBarTheme {
        val navController = rememberNavController()
        val userViewModel: UserViewModel = viewModel()
        RegisterScreen(navController, userViewModel)
    }
}
