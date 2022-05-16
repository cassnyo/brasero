package com.cassnyo.brasero.ui.common.navigation

object NavigationRoutes {
    const val FORECAST = "forecast/{townId}"
    fun forecast(townId: String) = FORECAST.replace("{townId}", townId)

    const val SEARCH = "search"
}