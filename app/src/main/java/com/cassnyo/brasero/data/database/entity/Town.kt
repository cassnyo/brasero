package com.cassnyo.brasero.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "town")
data class Town(
    @PrimaryKey
    val id: String,
    val townName: String,
    val provinceName: String,
    val isFavorite: Boolean
)