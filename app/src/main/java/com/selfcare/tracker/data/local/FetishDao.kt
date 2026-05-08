package com.selfcare.tracker.data.local

import androidx.room.*
import com.selfcare.tracker.data.model.Fetish
import kotlinx.coroutines.flow.Flow

@Dao
interface FetishDao {
    @Query("SELECT * FROM fetishes ORDER BY name ASC")
    fun getAllFetishes(): Flow<List<Fetish>>

    @Query("SELECT * FROM fetishes ORDER BY name ASC")
    suspend fun getAllFetishesList(): List<Fetish>

    @Query("SELECT * FROM fetishes WHERE id = :id")
    suspend fun getFetishById(id: Long): Fetish?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFetish(fetish: Fetish)

    @Update
    suspend fun updateFetish(fetish: Fetish)

    @Delete
    suspend fun deleteFetish(fetish: Fetish)

    @Query("DELETE FROM fetishes WHERE id = :id")
    suspend fun deleteFetishById(id: Long)

    @Query("DELETE FROM fetishes")
    suspend fun deleteAllFetishes()

    @Query("SELECT COUNT(*) FROM fetishes")
    suspend fun getFetishCount(): Int
}
