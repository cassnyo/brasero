package com.cassnyo.brasero.ui.screen.search

import com.cassnyo.brasero.data.database.entity.Town

data class SearchUiState(
    val query: String = "",
    val isLoading: Boolean = false,
    val isRefreshingTowns: Boolean = false,
    val towns: List<Town> = emptyList()
) {
    fun noResultsFound() = !isLoading && query.isNotEmpty() && towns.isEmpty()
}