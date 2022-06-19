package com.cassnyo.brasero.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.cassnyo.brasero.data.database.entity.DayForecast

@Dao
interface DayForecastDao {

    @Insert
    fun saveDayForecast(dayForecast: DayForecast)

    @Insert
    fun saveDailyForecast(dayForecast: List<DayForecast>)

    @Query("DELETE FROM day_forecast WHERE townId = :townId")
    fun deleteDailyForecastByTownId(townId: String)

}