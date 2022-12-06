package com.cassnyo.brasero.data.database.dao

import androidx.room.*
import com.cassnyo.brasero.data.database.entity.Town
import kotlinx.coroutines.flow.Flow

@Dao
interface TownDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveTown(town: Town)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveTowns(towns: List<Town>)

    @Query("DELETE FROM town WHERE id = :townId")
    suspend fun deleteTown(townId: String)

    @Query("SELECT * FROM town WHERE townName LIKE '%' || :name || '%'")
    fun getTowns(name: String): Flow<List<Town>>

    @Query("SELECT * FROM town WHERE isFavorite = 1")
    fun getFavoriteTowns(): Flow<List<Town>>

    @Update
    suspend fun updateTown(town: Town)

}