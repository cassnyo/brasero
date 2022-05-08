package com.cassnyo.brasero.ui.common.navigation

object NavigationRoutes {
    const val FORECAST = "forecast/{cityId}"
    fun forecast(cityId: String) = FORECAST.replace("{cityId}", cityId)

    const val SEARCH = "search"
}