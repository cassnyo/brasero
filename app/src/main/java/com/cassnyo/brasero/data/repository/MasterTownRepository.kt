package com.cassnyo.brasero.data.repository

import com.cassnyo.brasero.data.network.AemetApi
import com.cassnyo.brasero.ui.model.MasterTown
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MasterTownRepository @Inject constructor(
    private val aemetApi: AemetApi
) {

    private var masterTownsCache: List<MasterTown> = emptyList()

    suspend fun getMasterTowns(query: String): Flow<List<MasterTown>> = withContext(Dispatchers.Main) {
        if (masterTownsCache.isEmpty()) {
            masterTownsCache = aemetApi.getMasterTowns().map { it.toMasterTown() }
        }

        flowOf(masterTownsCache.filter { it.name.startsWith(prefix = query, ignoreCase = true) })
    }

}