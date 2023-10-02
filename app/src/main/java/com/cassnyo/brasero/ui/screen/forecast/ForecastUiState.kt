package com.cassnyo.brasero.ui.screen.forecast

import com.cassnyo.brasero.data.database.join.TownForecast

data class ForecastUiState(
    val isLoading: Boolean,
    val forecast: TownForecast
)