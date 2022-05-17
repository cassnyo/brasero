package com.cassnyo.brasero.ui.screen.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.cassnyo.brasero.ui.common.navigation.NavigationRoutes
import com.cassnyo.brasero.ui.screen.forecast.TownForecast
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState

@Composable
fun HomeScreen(navController: NavController) {
    val viewModel : HomeViewModel = hiltViewModel()
    val state by viewModel.state.collectAsState(initial = HomeViewModel.UiState())

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        val pagerState = rememberPagerState()

        Header(
            pagerState = pagerState,
            onSearchClicked = {
                navController.navigate(NavigationRoutes.SEARCH)
            },
            onDotsClicked = {
                // TODO
            }
        )

        HorizontalPager(
            count = state.townsForecast.size,
            state = pagerState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) { page ->
            TownForecast(forecast = state.townsForecast[page])
        }
    }
}

@Composable
private fun Header(
    pagerState: PagerState,
    onSearchClicked: () -> Unit,
    onDotsClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = onSearchClicked) {
            Icon(
                imageVector = Icons.Rounded.Search,
                contentDescription = "Buscar municipio"
            )
        }

        HorizontalPagerIndicator(
            pagerState = pagerState,
            modifier = Modifier
                .align(Alignment.Bottom)
                .padding(bottom = 16.dp),
            indicatorWidth = 4.dp
        )

        IconButton(onClick = onDotsClicked) {
            Icon(
                imageVector = Icons.Rounded.MoreVert,
                contentDescription = "Menú extendido"
            )
        }
    }
}