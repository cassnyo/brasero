package com.cassnyo.brasero.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cassnyo.brasero.ui.common.navigation.NavigationRoutes
import com.cassnyo.brasero.ui.screen.forecast.ForecastScreen
import com.cassnyo.brasero.ui.screen.home.HomeScreen
import com.cassnyo.brasero.ui.screen.search.SearchScreen


@Composable
fun BraseroNavigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = NavigationRoutes.HOME
    ) {
        composable(route = NavigationRoutes.HOME) {
            HomeScreen(navController)
        }
        composable(route = NavigationRoutes.FORECAST) {
            ForecastScreen(navController)
        }
        composable(route = NavigationRoutes.SEARCH) {
            SearchScreen(navController)
        }
    }
}