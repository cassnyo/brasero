package com.cassnyo.brasero.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cassnyo.brasero.data.database.join.TownForecast
import com.cassnyo.brasero.data.repository.TownForecastRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val townForecastRepository: TownForecastRepository
) : ViewModel() {

    data class UiState(
        val isLoading: Boolean = false,
        val townsForecast: List<TownForecast> = emptyList()
    )

    private val isLoading = MutableStateFlow(false)
    private val townsForecast: Flow<List<TownForecast>> = townForecastRepository
        .getFavoriteTownsForecast()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    val state: Flow<UiState> = combine(isLoading, townsForecast) { isLoading, townsForecast ->
        UiState(isLoading, townsForecast)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), UiState())

}