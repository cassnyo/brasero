package com.cassnyo.brasero.data.repository

import android.content.Context
import androidx.core.content.edit
import com.cassnyo.brasero.data.database.BraseroDatabase
import com.cassnyo.brasero.data.database.entity.Town
import com.cassnyo.brasero.data.network.AemetApi
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class TownRepository @Inject constructor(
    private val aemetApi: AemetApi,
    private val braseroDatabase: BraseroDatabase,
    @ApplicationContext private val appContext: Context
) {

    companion object {
        const val PREFERENCES_NAME = "master_towns_cache"
        const val KEY_VALID_UNTIL = "valid_until"
    }

    private val preferences = appContext.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
    private val timeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    fun getFavoriteTowns(): Flow<List<Town>> = braseroDatabase.townDao().getFavoriteTowns()

    suspend fun getTowns(query: String): Flow<List<Town>> = withContext(Dispatchers.IO) {
        braseroDatabase.townDao().getTowns(query)
    }

    suspend fun updateTown(town: Town) = withContext(Dispatchers.IO) {
        braseroDatabase.townDao().updateTown(town)
    }

    suspend fun refreshTowns() = withContext(Dispatchers.IO) {
        if (shouldRefreshTowns()) {
            val response = aemetApi.getMasterTowns().map { it.toTown() }
            braseroDatabase.townDao().saveTowns(response)
            updateCacheAvailability()
        }
    }

    private fun updateCacheAvailability() {
        // Valid for 7 days
        val validUntil = LocalDateTime.now().plusWeeks(1).format(timeFormatter)
        preferences.edit {
            putString(
                KEY_VALID_UNTIL,
                validUntil
            )
        }
    }

    private fun shouldRefreshTowns(): Boolean {
        val validUntilString = preferences.getString(KEY_VALID_UNTIL, null) ?: return true
        val validUntil = LocalDateTime.parse(validUntilString, timeFormatter)
        return LocalDateTime.now().isAfter(validUntil)
    }

}