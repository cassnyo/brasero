package com.cassnyo.brasero.data.database.dao

import androidx.room.*
import com.cassnyo.brasero.data.database.entity.Town
import kotlinx.coroutines.flow.Flow

@Dao
interface TownDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveTown(town: Town)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun saveTowns(towns: List<Town>)

    @Query("DELETE FROM town WHERE id = :townId")
    fun deleteTown(townId: String)

    @Query("SELECT * FROM town WHERE townName LIKE '%' || :name || '%'")
    fun getTowns(name: String): Flow<List<Town>>

    @Query("SELECT * FROM town WHERE isFavorite = 1")
    fun getFavoriteTowns(): Flow<List<Town>>

    @Update
    fun updateTown(town: Town)

}