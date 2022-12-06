package com.cassnyo.brasero.data.repository

import android.content.Context
import androidx.core.content.edit
import com.cassnyo.brasero.data.database.BraseroDatabase
import com.cassnyo.brasero.data.database.entity.Town
import com.cassnyo.brasero.data.network.AemetApi
import com.cassnyo.brasero.di.module.IoDispatcher
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class TownRepository @Inject constructor(
    private val aemetApi: AemetApi,
    private val braseroDatabase: BraseroDatabase,
    @ApplicationContext private val appContext: Context,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) {

    companion object {
        const val PREFERENCES_NAME = "master_towns_cache"
        const val KEY_VALID_UNTIL = "valid_until"
    }

    private val preferences = appContext.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
    private val timeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    fun getFavoriteTowns(): Flow<List<Town>> = braseroDatabase.townDao()
        .getFavoriteTowns()
        .flowOn(ioDispatcher)

    fun getTowns(query: String): Flow<List<Town>> = braseroDatabase.townDao()
        .getTowns(query)
        .flowOn(ioDispatcher)

    suspend fun updateTown(town: Town) = withContext(ioDispatcher) {
        Timber.d("Update town ${town.townName} with id ${town.id}")
        braseroDatabase.townDao().updateTown(town)
    }

    suspend fun refreshTowns() = withContext(ioDispatcher){
        if (!shouldRefreshTowns()) return@withContext

        updateTownsFromApi()
        updateCacheValidity()
    }

    private suspend fun updateTownsFromApi() {
        val response = aemetApi.getMasterTowns().map { it.toTown() }
        braseroDatabase.townDao().saveTowns(response)
        Timber.d("Updated ${response.size} towns")
    }

    private fun updateCacheValidity() {
        // Valid for 7 days
        val validUntil = LocalDateTime.now().plusWeeks(1).format(timeFormatter)
        Timber.d("Update towns cache validity until $validUntil")
        preferences.edit {
            putString(KEY_VALID_UNTIL, validUntil)
        }
    }

    private fun shouldRefreshTowns(): Boolean {
        val validUntilString = preferences.getString(KEY_VALID_UNTIL, null) ?: return true
        val validUntil = LocalDateTime.parse(validUntilString, timeFormatter)
        Timber.d("Towns cache valid until $validUntilString")
        return LocalDateTime.now().isAfter(validUntil)
    }

}