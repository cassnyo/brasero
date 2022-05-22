package com.cassnyo.brasero.data.repository

import android.content.Context
import androidx.core.content.edit
import com.cassnyo.brasero.data.database.BraseroDatabase
import com.cassnyo.brasero.data.database.entity.Town
import com.cassnyo.brasero.data.network.AemetApi
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
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

    private val prefs = appContext.getSharedPreferences("prefs", Context.MODE_PRIVATE)

    // FIXME Improve caching
    suspend fun getMasterTowns(query: String): Flow<List<Town>> = withContext(Dispatchers.IO) {
        if (!cacheIsValid()) {
            Timber.d("Cache is not valid. Fetching from API")
            launch {
                val response = aemetApi.getMasterTowns().map { it.toTown() }
                braseroDatabase.townDao().saveTowns(response)
                prefs.edit {
                    putString(
                        "valid_until",
                        LocalDateTime.now().plusDays(1).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                    )
                }
            }
        } else {
            Timber.d("Cache is valid")
        }
        braseroDatabase.townDao().getTowns(query)
    }

    suspend fun updateTown(town: Town) = withContext(Dispatchers.IO) {
        braseroDatabase.townDao().updateTown(town)
    }

    private fun cacheIsValid(): Boolean {
        val cacheValidUntilString = prefs.getString("valid_until", null) ?: return false
        val cacheValidUntil = LocalDateTime.parse(cacheValidUntilString, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        return cacheValidUntil.isBefore(LocalDateTime.now())
    }

}