package com.cassnyo.brasero.ui.screen.forecast

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cassnyo.brasero.common.extension.isTodayOrAfter
import com.cassnyo.brasero.common.extension.isWithinNext24Hours
import com.cassnyo.brasero.data.database.entity.DayForecast
import com.cassnyo.brasero.data.database.entity.HourForecast
import com.cassnyo.brasero.data.database.join.TownForecast
import com.cassnyo.brasero.data.repository.TownForecastRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ForecastViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    townForecastRepository: TownForecastRepository
) : ViewModel() {

    val townId = savedStateHandle.get<String>("townId").orEmpty()
    val townForecast: StateFlow<TownForecast?> = townForecastRepository
        .observeTownForecast(townId)
        .distinctUntilChanged()
        .mapLatest { townForecast ->
            Timber.d("Map forecast for town ${townForecast.town.id}")
            townForecast.copy(
                hours = townForecast.hours.filterNextHours(),
                days = townForecast.days.filterNextDays()
            )
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    init {
        viewModelScope.launch {
            townForecastRepository.refreshTownForecast(townId)
        }
    }

    private fun List<HourForecast>.filterNextHours(): List<HourForecast> =
        filter { hourForecast -> hourForecast.date.isWithinNext24Hours() }

    private fun List<DayForecast>.filterNextDays(): List<DayForecast> =
        filter { dayForecast -> dayForecast.date.isTodayOrAfter() }
}