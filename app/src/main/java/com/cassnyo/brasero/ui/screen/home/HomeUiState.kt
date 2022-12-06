package com.cassnyo.brasero.ui.screen.home

import com.cassnyo.brasero.data.database.entity.Town

data class HomeUiState(
    val isLoading: Boolean = false,
    val favoriteTowns: List<Town> = emptyList()
) {
    companion object {
        val Loading = HomeUiState(isLoading = true)
    }
}