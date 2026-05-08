package com.selfcare.tracker.data.local

import androidx.room.*
import com.selfcare.tracker.data.model.Record
import kotlinx.coroutines.flow.Flow

@Dao
interface RecordDao {
    @Query("SELECT * FROM records ORDER BY startTime DESC")
    fun getAllRecords(): Flow<List<Record>>

    @Query("SELECT * FROM records WHERE startTime >= :startTime AND startTime <= :endTime ORDER BY startTime DESC")
    fun getRecordsByDateRange(startTime: Long, endTime: Long): Flow<List<Record>>

    @Query("SELECT * FROM records WHERE id = :id")
    suspend fun getRecordById(id: Long): Record?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecord(record: Record)

    @Update
    suspend fun updateRecord(record: Record)

    @Delete
    suspend fun deleteRecord(record: Record)

    @Query("DELETE FROM records WHERE id = :id")
    suspend fun deleteRecordById(id: Long)

    @Query("DELETE FROM records")
    suspend fun deleteAllRecords()

    @Query("SELECT COUNT(*) FROM records")
    suspend fun getRecordCount(): Int

    @Query("SELECT * FROM records ORDER BY startTime DESC LIMIT 1")
    suspend fun getLastRecord(): Record?

    @Query("SELECT COUNT(*) FROM records WHERE startTime >= :startTime AND startTime <= :endTime")
    suspend fun getRecordCountByDateRange(startTime: Long, endTime: Long): Int
}
