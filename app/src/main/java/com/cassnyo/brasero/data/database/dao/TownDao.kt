package com.cassnyo.brasero.data.database.dao

import androidx.room.*
import com.cassnyo.brasero.data.database.entity.Town

@Dao
interface TownDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveForecast(town: Town)

    @Query("DELETE FROM forecast WHERE id = :townId")
    fun deleteForecast(townId: String)

}