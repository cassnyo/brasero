package com.cassnyo.brasero.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.cassnyo.brasero.data.database.join.ForecastDetail
import kotlinx.coroutines.flow.Flow

@Dao
interface ForecastDetailDao {

    @Transaction
    @Query("SELECT * FROM forecast WHERE id = :townId")
    fun getForecastDetailByTown(townId: String): Flow<ForecastDetail>

}