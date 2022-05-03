package com.cassnyo.brasero.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import com.cassnyo.brasero.data.database.entity.HourForecast

@Dao
interface HourForecastDao {

    @Insert
    fun saveHourForecast(hourForecast: HourForecast)

    @Insert
    fun saveHourlyForecast(hourForecast: List<HourForecast>)

}