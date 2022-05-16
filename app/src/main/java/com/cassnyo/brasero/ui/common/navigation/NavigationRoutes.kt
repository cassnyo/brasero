package com.cassnyo.brasero.ui.common.navigation

object NavigationRoutes {
    const val HOME = "home"
    const val SEARCH = "search"
    const val FORECAST = "forecast/{townId}"
    fun forecast(townId: String) = FORECAST.replace("{townId}", townId)
}