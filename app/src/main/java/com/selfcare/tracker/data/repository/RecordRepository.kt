package com.selfcare.tracker.data.repository

import com.selfcare.tracker.data.local.RecordDao
import com.selfcare.tracker.data.model.Record
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecordRepository @Inject constructor(
    private val recordDao: RecordDao
) {
    fun getAllRecords(): Flow<List<Record>> = recordDao.getAllRecords()

    fun getRecordsByDateRange(startTime: Long, endTime: Long): Flow<List<Record>> =
        recordDao.getRecordsByDateRange(startTime, endTime)

    suspend fun getRecordById(id: Long): Record? = recordDao.getRecordById(id)

    suspend fun insertRecord(record: Record) = recordDao.insertRecord(record)

    suspend fun updateRecord(record: Record) = recordDao.updateRecord(record)

    suspend fun deleteRecord(record: Record) = recordDao.deleteRecord(record)

    suspend fun deleteRecordById(id: Long) = recordDao.deleteRecordById(id)

    suspend fun deleteAllRecords() = recordDao.deleteAllRecords()

    suspend fun getRecordCount(): Int = recordDao.getRecordCount()

    suspend fun getLastRecord(): Record? = recordDao.getLastRecord()

    suspend fun getRecordCountByDateRange(startTime: Long, endTime: Long): Int =
        recordDao.getRecordCountByDateRange(startTime, endTime)

    suspend fun getAllRecordsList(): List<Record> {
        var records = emptyList<Record>()
        getAllRecords().collect { records = it }
        return records
    }
}
