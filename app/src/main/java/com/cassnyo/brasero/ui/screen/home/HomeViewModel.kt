package com.cassnyo.brasero.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cassnyo.brasero.data.database.entity.Town
import com.cassnyo.brasero.data.repository.TownRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val townRepository: TownRepository
) : ViewModel() {

    data class UiState(
        val isLoading: Boolean = false,
        val favoriteTowns: List<Town> = emptyList()
    )

    private val isLoading = MutableStateFlow(false)
    private val favoriteTowns: Flow<List<Town>> = townRepository
        .getFavoriteTowns()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    val state: Flow<UiState> = combine(isLoading, favoriteTowns) { isLoading, townsForecast ->
        UiState(isLoading, townsForecast)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), UiState())

}