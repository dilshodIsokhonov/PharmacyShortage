package com.example.pharmacyshortage.data

import androidx.room.*
import com.example.pharmacyshortage.model.Shortage
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for [Shortage] entity
 */
@Dao
interface ShortageDao {

    // Gets a shortage
    @Query("SELECT * FROM shortage_table WHERE shortage_id = :id")
    fun getShortage(id: Long): Flow<Shortage>

    // Gets a lit of shortages by name ASC
    @Query("SELECT * FROM shortage_table ORDER BY name ASC")
    fun getShortagesByName(): Flow<List<Shortage>>

    // Gets a lit of shortages by name DESC
    @Query("SELECT * FROM shortage_table ORDER BY name DESC")
    fun getShortagesByNameDesc(): Flow<List<Shortage>>

    // Gets a list of shortages by type
    @Query("SELECT * FROM shortage_table ORDER BY type")
    fun getShortagesByType(): Flow<List<Shortage>>

    // Gets a list of shortages by degree
    @Query("SELECT * FROM shortage_table  ORDER BY important DESC , name ASC")
    fun getShortagesByImportance(): Flow<List<Shortage>>

    // Gets a count of all shortages
    @Query("SELECT  COUNT(*) FROM shortage_table")
    fun getShortageCount(): Flow<Long>

    @Query("DELETE FROM shortage_table")
    suspend fun deleteAll()


    // Adds new shortage to shortage_table
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertShortage(vararg shortage: Shortage)



    // Makes a change in shortage_table
    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun updateShortage(vararg shortage: Shortage)

    // Deletes one or more shortage objects
    @Delete
    suspend fun deleteShortage(vararg shortage: Shortage)

}