package com.cassnyo.brasero.data.database.dao

import androidx.room.*
import com.cassnyo.brasero.data.database.entity.Town

@Dao
interface ForecastDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveForecast(town: Town)

    @Query("DELETE FROM forecast WHERE id = :forecastId")
    fun deleteForecast(forecastId: String)

}