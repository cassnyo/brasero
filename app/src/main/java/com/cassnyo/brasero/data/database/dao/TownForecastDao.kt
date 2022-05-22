package com.cassnyo.brasero.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.cassnyo.brasero.data.database.join.TownForecast
import kotlinx.coroutines.flow.Flow

@Dao
interface TownForecastDao {

    @Transaction
    @Query("SELECT * FROM town WHERE id = :townId")
    fun getTownForecastById(townId: String): Flow<TownForecast>

    @Transaction
    @Query("SELECT * FROM town WHERE isFavorite = 1")
    fun getFavoriteTownsForecast(): Flow<List<TownForecast>>

}