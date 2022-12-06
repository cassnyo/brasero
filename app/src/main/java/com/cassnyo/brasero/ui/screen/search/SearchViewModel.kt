package com.cassnyo.brasero.ui.screen.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cassnyo.brasero.data.database.entity.Town
import com.cassnyo.brasero.data.repository.TownRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val townRepository: TownRepository
) : ViewModel() {

    private val query = MutableStateFlow("")
    private val isLoading = MutableStateFlow(false)
    private val isRefreshingTowns = MutableStateFlow(false)
    private val towns: Flow<List<Town>> = query
        .debounce(250)
        .onEach { isLoading.value = true }
        .flatMapLatest { query ->
            when {
                query.isEmpty() -> emptyFlow()
                else -> queryTowns(query)
            }
        }
        .onEach { isLoading.value = false }
        .stateIn(viewModelScope, WhileSubscribed(5000), emptyList())

    val uiState: Flow<SearchUiState> = combine(
        query, isLoading, isRefreshingTowns, towns
    ) { query, isLoading, isRefreshingTowns, towns ->
        SearchUiState(query, isLoading, isRefreshingTowns, towns)
    }.stateIn(viewModelScope, WhileSubscribed(5000), SearchUiState())

    init {
        refreshTowns()
    }

    private fun refreshTowns() {
        isRefreshingTowns.value = true
        viewModelScope.launch {
            townRepository.refreshTowns()
            isRefreshingTowns.value = false
        }
    }

    private fun queryTowns(query: String): Flow<List<Town>> =
        townRepository
            .getTowns(query)
            .distinctUntilChanged()
            .onStart { isLoading.value = true }

    fun onQueryChanged(query: String) {
        this.query.value = query
    }

    fun onClearQueryClicked() {
        this.query.value = ""
    }

    fun onAddTownClicked(town: Town) {
        viewModelScope.launch {
            townRepository.updateTown(town.copy(isFavorite = true))
        }
    }
}