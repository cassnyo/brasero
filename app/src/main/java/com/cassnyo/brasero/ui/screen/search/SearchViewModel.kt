package com.cassnyo.brasero.ui.screen.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cassnyo.brasero.data.database.entity.Town
import com.cassnyo.brasero.data.repository.TownRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val townRepository: TownRepository
) : ViewModel() {

    data class UiState(
        val query: String = "",
        val isLoading: Boolean = false,
        val isRefreshingTowns: Boolean = false,
        val towns: List<Town> = emptyList()
    )

    private val currentQuery = MutableStateFlow("")
    private val isLoading = MutableStateFlow(false)
    private val isRefreshingTowns = MutableStateFlow(false)
    private val towns: Flow<List<Town>> = currentQuery
        .onEach { query ->
            if (query.isNotEmpty()) {
                isLoading.value = true
            }
        }
        .flatMapLatest { query ->
            when {
                query.isEmpty() -> flowOf(emptyList())
                else -> townRepository.getTowns(query)
            }
        }
        .onEach {
            isLoading.value = false
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), listOf())

    val state: Flow<UiState> = combine(
        currentQuery,
        isLoading,
        isRefreshingTowns,
        towns
    ) { currentQuery, isLoading, isRefreshingTowns, towns ->
        UiState(currentQuery, isLoading, isRefreshingTowns, towns)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), UiState())

    init {
        viewModelScope.launch {
            isRefreshingTowns.value = true
            townRepository.refreshTowns()
            isRefreshingTowns.value = false
        }
    }

    fun onQueryChanged(query: String) {
        currentQuery.value = query
    }

    fun onClearQueryClicked() {
        currentQuery.value = ""
    }

    fun onBackClicked() {
        // TODO
    }

    fun onAddTownClicked(town: Town) {
        viewModelScope.launch {
            townRepository.updateTown(town.copy(isFavorite = true))
        }
    }

}