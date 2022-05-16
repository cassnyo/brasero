package com.cassnyo.brasero.data.database.dao

import androidx.room.*
import com.cassnyo.brasero.data.database.entity.Town
import kotlinx.coroutines.flow.Flow

@Dao
interface TownDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveTown(town: Town)

    @Query("DELETE FROM town WHERE id = :townId")
    fun deleteTown(townId: String)

    @Query("SELECT * FROM town")
    fun getTowns(): Flow<List<Town>>

}