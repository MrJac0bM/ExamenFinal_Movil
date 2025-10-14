package com.example.examfinal.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.examfinal.presentation.CountryViewModel
import com.example.examfinal.views.ListMainScreen

sealed class Screen(val route: String) {
    object Search : Screen("search")
    object List : Screen("list")
    //object MovieDetail : Screen("movie_detail/{movieId}") {
    //    fun createRoute(movieId: String) = "movie_detail/$movieId"
   // }
}

@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController(),
    startDestination: String = Screen.List.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.List.route) {
            val viewModel: CountryViewModel = hiltViewModel()
            ListMainScreen(
                navController = navController,
                viewModel = viewModel
            )
        }

    }
}

