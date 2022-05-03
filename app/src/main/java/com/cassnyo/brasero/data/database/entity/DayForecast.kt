package com.cassnyo.brasero.data.database.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(
    tableName = "day_forecast",
    foreignKeys = [
        ForeignKey(
            entity = Town::class,
            parentColumns = ["id"],
            childColumns = ["townId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class DayForecast(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val townId: String,
    val date: LocalDate,
    val skyStatus: String,
    val chanceOfRain: Int,
    @Embedded
    val temperature: Temperature,
    @Embedded
    val wind: Wind,
    @Embedded
    val humidity: Humidity

)