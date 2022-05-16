package com.cassnyo.brasero.ui.screen.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cassnyo.brasero.data.repository.MasterTownRepository
import com.cassnyo.brasero.ui.model.MasterTown
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val masterTownRepository: MasterTownRepository
) : ViewModel() {

    data class UiState(
        val query: String = "",
        val isLoading: Boolean = false,
        val masterTowns: List<MasterTown> = emptyList()
    )

    private val currentQuery = MutableStateFlow("")
    private val isLoading = MutableStateFlow(false)
    private val masterTowns: Flow<List<MasterTown>> = currentQuery
        .debounce(125L)
        .onEach { query ->
            if (query.isNotEmpty()) {
                isLoading.value = true
            }
        }
        .flatMapLatest { query ->
            when {
                query.isEmpty() -> flowOf(emptyList())
                else -> masterTownRepository.getMasterTowns(query)
            }
        }
        .onEach {
            isLoading.value = false
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), listOf())

    val state: Flow<UiState> = combine(
        currentQuery,
        isLoading,
        masterTowns
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

    fun onAddTownClicked(town: MasterTown) {
        // TODO
    }

}