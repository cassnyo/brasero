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
    private val forecastRepository: TownRepository
) : ViewModel() {

    data class UiState(
        val isLoading: Boolean = false,
        val towns: List<Town> = emptyList()
    )

    private val isLoading = MutableStateFlow(false)
    private val towns: Flow<List<Town>> = forecastRepository
        .getTowns()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    val state: Flow<UiState> = combine(isLoading, towns) { loading, towns ->
        UiState(loading, towns)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), UiState())

}