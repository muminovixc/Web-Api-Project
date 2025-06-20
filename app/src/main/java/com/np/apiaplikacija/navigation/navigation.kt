package com.np.apiaplikacija.navigation

import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
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

@Composable
fun AppNavigation(navController: NavHostController = rememberNavController()) {
    val currentBackStack by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStack?.destination?.route

    val noSidebarRoutes = listOf("login", "register")

    val content: @Composable () -> Unit = {
        NavHost(navController = navController, startDestination = "login") {
            composable("login") {

                LoginScreen(navController) }
            composable("register") { RegisterScreen(navController) }
            composable("home") {
                val viewModel: HomePageViewModel = viewModel()
                HomePage(viewModel,navController,token = "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiIyMDczIiwibmJmIjoxNzUwMzY3ODYxLCJleHAiOjE3NTA0NTQyNjEsImlhdCI6MTc1MDM2Nzg2MX0.wu6JtsUO9Vx_kN46Of7Yae__s3CwYj1ZURd3ubInvhXlU6QS52qzzb5uat3wHhwDZTLlpbEzBPbDnQhN3E1-sw") }
            composable("datasets") {
                val viewModel: ApiViewModel = viewModel()
                DatasetListScreen(
                    viewModel,
                    token = "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiIyMDczIiwibmJmIjoxNzUwMzY3ODYxLCJleHAiOjE3NTA0NTQyNjEsImlhdCI6MTc1MDM2Nzg2MX0.wu6JtsUO9Vx_kN46Of7Yae__s3CwYj1ZURd3ubInvhXlU6QS52qzzb5uat3wHhwDZTLlpbEzBPbDnQhN3E1-sw",
                    navController
                )
            }
            composable("dataset_detail/{url}") { backStackEntry ->
                val viewModel: DatasetDetailViewModel=viewModel()
                val url = backStackEntry.arguments?.getString("url") ?: ""
                DatasetDetailScreen(url = url,viewModel,navController)
            }
            composable(
                route = "statistika/{data}",
                arguments = listOf(navArgument("data") { type = NavType.StringType })
            ) { backStackEntry ->
                StatistikaScreen(backStackEntry)
            }
            composable("favorites") {
                val apiViewModel: ApiViewModel=viewModel()
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
