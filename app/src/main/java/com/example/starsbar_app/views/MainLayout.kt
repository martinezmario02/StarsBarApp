package com.example.starsbar_app.views

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController

@Composable
fun MainLayout(
    navController: NavController,
    title: String = "StarsBar",
    showBackButton: Boolean = true,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Header(
            navController = navController,
            showBackButton = showBackButton,
            title = title
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ) {
            content()
        }
    }
}

