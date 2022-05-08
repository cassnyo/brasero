package com.cassnyo.brasero

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cassnyo.brasero.ui.common.navigation.NavigationRoutes
import com.cassnyo.brasero.ui.screen.forecast.ForecastScreen
import com.cassnyo.brasero.ui.screen.search.SearchScreen
import com.cassnyo.brasero.ui.theme.BraseroTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BraseroTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = NavigationRoutes.SEARCH
                    ) {
                        composable(route = NavigationRoutes.FORECAST) {
                            ForecastScreen(navController)
                        }
                        composable(route = NavigationRoutes.SEARCH) {
                            SearchScreen(navController)
                        }
                    }
                }
            }
        }
    }
}