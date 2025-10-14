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
import com.example.examfinal.views.CountryDetailScreen
import com.example.examfinal.views.ListMainScreen

sealed class Screen(val route: String) {
    object CountryList : Screen("country_list")
    object CountryDetail : Screen("country_detail/{countryName}") {
        fun createRoute(countryName: String) = "country_detail/$countryName"
    }
}

// NavGraph.kt
@Composable
fun NavGraph(
    navController: NavHostController,
    viewModel: CountryViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Screen.CountryList.route
    ) {
        composable(Screen.CountryList.route) {
            ListMainScreen(
                navController = navController,
                viewModel = viewModel
            )
        }

        composable(
            route = Screen.CountryDetail.route,
            arguments = listOf(
                navArgument("countryName") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val countryName = backStackEntry.arguments?.getString("countryName") ?: ""
            CountryDetailScreen(
                navController = navController,
                viewModel = viewModel,
                countryName = countryName
            )
        }
    }
}