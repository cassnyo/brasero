package com.cassnyo.brasero.data.database.join

import androidx.room.Embedded
import androidx.room.Relation
import com.cassnyo.brasero.data.database.entity.DayForecast
import com.cassnyo.brasero.data.database.entity.Town
import com.cassnyo.brasero.data.database.entity.HourForecast

class ForecastDetail(
    @Embedded val town: Town,
    @Relation(
        parentColumn = "id",
        entityColumn = "townId"
    )
    val hours: List<HourForecast>,
    @Relation(
        parentColumn = "id",
        entityColumn = "townId"
    )
    val days: List<DayForecast>
)