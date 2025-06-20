package com.np.apiaplikacija.navigation

import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.*
import androidx.navigation.compose.*
import com.np.apiaplikacija.components.SideBar
import com.np.apiaplikacija.frontend.w.dataset.DatasetDetailScreen
import com.np.apiaplikacija.frontend.w.dataset.DatasetListScreen
import com.np.apiaplikacija.frontend.w.favorites.FavoritesScreen
import com.np.apiaplikacija.frontend.w.homepage.HomePage
import com.np.apiaplikacija.frontend.w.registration.RegisterScreen
import com.np.apiaplikacija.frontend.w.login.LoginScreen
import com.np.apiaplikacija.frontend.w.statistics.StatistikaScreen
import com.np.apiaplikacija.viewmodel.ApiViewModel
import com.np.apiaplikacija.viewmodel.DatasetDetailViewModel
import com.np.apiaplikacija.viewmodel.HomePageViewModel
import kotlinx.coroutines.delay
import androidx.compose.material3.*
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.np.apiaplikacija.frontend.w.splashscreen.SplashScreen

@Composable
fun AppNavigation(navController: NavHostController = rememberNavController()) {
    val currentBackStack by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStack?.destination?.route

    val noSidebarRoutes = listOf("login", "register", "splash")

    val content: @Composable () -> Unit = {
        NavHost(navController = navController, startDestination = "splash") {
            composable("splash") {
                SplashScreen(navController)
            }
            composable("login") {
                LoginScreen(navController)
            }
            composable("register") {
                RegisterScreen(navController)
            }
            composable("home") {
                val viewModel: HomePageViewModel = viewModel()
                HomePage(navController)
            }
            composable("datasets") {
                val viewModel: ApiViewModel = viewModel()
                DatasetListScreen(
                    viewModel,
                    token = "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiIyMDczIiwibmJmIjoxNzUwNDU1OTk2LCJleHAiOjE3NTA1NDIzOTYsImlhdCI6MTc1MDQ1NTk5Nn0.bcei5S82zluoNBlgj2Ge1eVnayuJemWTGPUCQ2S8W3M8UI9RTHt1_cEh8x9Edn0-YrJ1O6v2un-fbonRpdHmSg",
                    navController
                )
            }
            composable("dataset_detail/{url}") { backStackEntry ->
                val viewModel: DatasetDetailViewModel = viewModel()
                val url = backStackEntry.arguments?.getString("url") ?: ""
                DatasetDetailScreen(url = url, viewModel, navController)
            }
            composable(
                route = "statistika/{data}",
                arguments = listOf(navArgument("data") { type = NavType.StringType })
            ) { backStackEntry ->
                StatistikaScreen(backStackEntry)
            }
            composable("favorites") {
                val apiViewModel: ApiViewModel = viewModel()
                FavoritesScreen(viewModel = apiViewModel, navController = navController)
            }
        }
    }

    if (currentRoute in noSidebarRoutes) {
        content()
    } else {
        SideBar(navController = navController) {
            content()
        }
    }
}

