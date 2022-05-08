package com.cassnyo.brasero.ui.screen.forecast

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cassnyo.brasero.data.database.join.ForecastDetail
import com.cassnyo.brasero.data.repository.ForecastRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForecastViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val forecastRepository: ForecastRepository
): ViewModel() {

    val townId = savedStateHandle.get<String>("cityId").orEmpty()
    val forecast: Flow<ForecastDetail?> = forecastRepository.getForecastDetailByTown(townId).flowOn(Dispatchers.IO)

    init {
        refreshForecast()
    }

    private fun refreshForecast() {
        viewModelScope.launch(Dispatchers.IO) {
            forecastRepository.refreshForecastByTown(townId)
        }
    }

}