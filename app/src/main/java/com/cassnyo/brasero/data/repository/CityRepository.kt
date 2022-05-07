package com.cassnyo.brasero.data.repository

import com.cassnyo.brasero.data.network.AemetApi
import com.cassnyo.brasero.ui.model.City
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CityRepository @Inject constructor(
    private val aemetApi: AemetApi
) {

    private var cityCache: List<City> = emptyList()

    suspend fun getCities(query: String): Flow<List<City>> = withContext(Dispatchers.Main) {
        if (cityCache.isEmpty()) {
            cityCache = aemetApi.getCities().map { it.toCity() }
        }

        flowOf(cityCache.filter { it.name.startsWith(prefix = query, ignoreCase = true) })
    }

}