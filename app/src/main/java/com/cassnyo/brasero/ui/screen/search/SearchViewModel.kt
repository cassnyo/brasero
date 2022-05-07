package com.cassnyo.brasero.ui.screen.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cassnyo.brasero.data.repository.CityRepository
import com.cassnyo.brasero.ui.model.City
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val cityRepository: CityRepository
) : ViewModel() {

    data class UiState(
        val query: String = "",
        val isLoading: Boolean = false,
        val cities: List<City> = emptyList()
    )

    private val currentQuery = MutableStateFlow("")
    private val isLoading = MutableStateFlow(false)
    private val cities: Flow<List<City>> = currentQuery
        .debounce(125L)
        .onEach { query ->
            if (query.isNotEmpty()) {
                isLoading.value = true
            }
        }
        .flatMapLatest { query ->
            when {
                query.isEmpty() -> emptyFlow()
                else -> cityRepository.getCities(query)
            }
        }
        .onEach {
            isLoading.value = false
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), listOf())

    val state: Flow<UiState> = combine(
        currentQuery,
        isLoading,
        cities
    ) { query, loading, cities ->
        UiState(query, loading, cities)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), UiState())

    fun onQueryChanged(query: String) {
        currentQuery.value = query
    }

    fun onClearQueryClicked() {
        currentQuery.value = ""
    }

    fun onBackClicked() {
        // TODO
    }

    fun onCityClicked(city: City) {
        // TODO
    }

    fun onAddCityClicked(city: City) {
        // TODO
    }

}