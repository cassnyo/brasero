package com.cassnyo.brasero.data.database.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(
    tableName = "hour_forecast",
    foreignKeys = [
        ForeignKey(
            entity = Town::class,
            parentColumns = ["id"],
            childColumns = ["townId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class HourForecast(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val townId: String,
    val date: LocalDateTime,
    val skyStatus: String,
    val chanceOfRain: Int,
    val rain: Int,
    val temperature: Int,
    @Embedded
    val wind: Wind,
    val humidity: Int
)