package com.cassnyo.brasero

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.ui.Modifier
import com.cassnyo.brasero.ui.navigation.BraseroNavigation
import com.cassnyo.brasero.ui.theme.BraseroTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BraseroTheme {
                // NavigationCompose does not show startDestination on Xiaomi Device unless you use a Scaffold
                // https://issuetracker.google.com/issues/227926002
                Scaffold(modifier = Modifier.fillMaxSize()) {
                    BraseroNavigation()
                }
            }
        }
    }
}