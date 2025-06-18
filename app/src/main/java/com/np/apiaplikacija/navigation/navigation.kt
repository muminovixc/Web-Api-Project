package com.np.apiaplikacija.navigation

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.np.apiaplikacija.components.SideBar
import com.np.apiaplikacija.homepage.HomePage
import com.np.apiaplikacija.splashscreen.SplashScreen
import kotlinx.coroutines.delay

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    var showSplash by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        delay(2000)
        showSplash = false
    }

    if (showSplash) {
        SplashScreen()
    } else {
        SideBar(navController = navController) {
            NavHost(navController = navController, startDestination = "home") {
                composable("home") { HomePage(navController) }
                // Dodaj više ruta ovdje ako treba
            }
        }
    }
}
