package com.selfcare.tracker.data.repository

import com.selfcare.tracker.data.local.MediumDao
import com.selfcare.tracker.data.model.Medium
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MediumRepository @Inject constructor(
    private val mediumDao: MediumDao
) {
    fun getAllMedia(): Flow<List<Medium>> = mediumDao.getAllMedia()

    suspend fun getAllMediaList(): List<Medium> = mediumDao.getAllMediaList()

    suspend fun getMediumById(id: Long): Medium? = mediumDao.getMediumById(id)

    suspend fun insertMedium(medium: Medium) = mediumDao.insertMedium(medium)

    suspend fun updateMedium(medium: Medium) = mediumDao.updateMedium(medium)

    suspend fun deleteMedium(medium: Medium) = mediumDao.deleteMedium(medium)

    suspend fun deleteMediumById(id: Long) = mediumDao.deleteMediumById(id)

    suspend fun deleteAllMedia() = mediumDao.deleteAllMedia()

    suspend fun getMediumCount(): Int = mediumDao.getMediumCount()
}
