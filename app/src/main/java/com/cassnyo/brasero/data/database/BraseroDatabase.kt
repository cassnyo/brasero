package com.cassnyo.brasero.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.cassnyo.brasero.data.database.converter.LocalDateConverter
import com.cassnyo.brasero.data.database.converter.LocalDateTimeConverter
import com.cassnyo.brasero.data.database.dao.DayForecastDao
import com.cassnyo.brasero.data.database.dao.ForecastDao
import com.cassnyo.brasero.data.database.dao.ForecastDetailDao
import com.cassnyo.brasero.data.database.dao.HourForecastDao
import com.cassnyo.brasero.data.database.entity.DayForecast
import com.cassnyo.brasero.data.database.entity.Town
import com.cassnyo.brasero.data.database.entity.HourForecast

@Database(
    entities = [
        Town::class,
        HourForecast::class,
        DayForecast::class
    ],
    version = 1
)
@TypeConverters(
    LocalDateConverter::class,
    LocalDateTimeConverter::class
)
abstract class BraseroDatabase: RoomDatabase() {

    abstract fun forecastDao(): ForecastDao
    abstract fun hourForecast(): HourForecastDao
    abstract fun dayForecast(): DayForecastDao
    abstract fun forecastDetailDao(): ForecastDetailDao

}