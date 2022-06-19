package com.cassnyo.brasero.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.cassnyo.brasero.data.database.entity.HourForecast

@Dao
interface HourForecastDao {

    @Insert
    fun saveHourForecast(hourForecast: HourForecast)

    @Insert
    fun saveHourlyForecast(hourForecast: List<HourForecast>)

    @Query("DELETE FROM hour_forecast WHERE townId = :townId")
    fun deleteHourlyForecastByTownId(townId: String)

}