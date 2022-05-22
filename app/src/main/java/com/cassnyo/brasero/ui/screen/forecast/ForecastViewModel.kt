package com.cassnyo.brasero.ui.screen.forecast

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cassnyo.brasero.data.database.join.TownForecast
import com.cassnyo.brasero.data.repository.TownForecastRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForecastViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val townForecastRepository: TownForecastRepository
): ViewModel() {

    val townId = savedStateHandle.get<String>("townId").orEmpty()
    val townForecast: Flow<TownForecast?> = townForecastRepository.getTownForecast(townId).flowOn(Dispatchers.IO)

    init {
        refreshForecast()
    }

    private fun refreshForecast() {
        viewModelScope.launch(Dispatchers.IO) {
            townForecastRepository.refreshTownForecast(townId)
        }
    }

}