package com.selfcare.tracker.data.local

import androidx.room.*
import com.selfcare.tracker.data.model.Medium
import kotlinx.coroutines.flow.Flow

@Dao
interface MediumDao {
    @Query("SELECT * FROM media ORDER BY name ASC")
    fun getAllMedia(): Flow<List<Medium>>

    @Query("SELECT * FROM media ORDER BY name ASC")
    suspend fun getAllMediaList(): List<Medium>

    @Query("SELECT * FROM media WHERE id = :id")
    suspend fun getMediumById(id: Long): Medium?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMedium(medium: Medium)

    @Update
    suspend fun updateMedium(medium: Medium)

    @Delete
    suspend fun deleteMedium(medium: Medium)

    @Query("DELETE FROM media WHERE id = :id")
    suspend fun deleteMediumById(id: Long)

    @Query("DELETE FROM media")
    suspend fun deleteAllMedia()

    @Query("SELECT COUNT(*) FROM media")
    suspend fun getMediumCount(): Int
}
