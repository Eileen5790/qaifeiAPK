package com.selfcare.tracker.data.repository

import com.selfcare.tracker.data.local.FetishDao
import com.selfcare.tracker.data.model.Fetish
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FetishRepository @Inject constructor(
    private val fetishDao: FetishDao
) {
    fun getAllFetishes(): Flow<List<Fetish>> = fetishDao.getAllFetishes()

    suspend fun getAllFetishesList(): List<Fetish> = fetishDao.getAllFetishesList()

    suspend fun getFetishById(id: Long): Fetish? = fetishDao.getFetishById(id)

    suspend fun insertFetish(fetish: Fetish) = fetishDao.insertFetish(fetish)

    suspend fun updateFetish(fetish: Fetish) = fetishDao.updateFetish(fetish)

    suspend fun deleteFetish(fetish: Fetish) = fetishDao.deleteFetish(fetish)

    suspend fun deleteFetishById(id: Long) = fetishDao.deleteFetishById(id)

    suspend fun deleteAllFetishes() = fetishDao.deleteAllFetishes()

    suspend fun getFetishCount(): Int = fetishDao.getFetishCount()
}
