package com.example.pharmacyshortage.data

import androidx.annotation.WorkerThread
import com.example.pharmacyshortage.model.Shortage
import kotlinx.coroutines.flow.Flow

/**
 * This class is responsible for providing data to ViewModel
 */

class ShortageRepository(private val shortageDao: ShortageDao) {

    fun getShortage(id: Long): Flow<Shortage> = shortageDao.getShortage(id)

    val allShortagesByNameAsc: Flow<List<Shortage>> = shortageDao.getShortagesByName()

    val allShortagesByNameDesc: Flow<List<Shortage>> = shortageDao.getShortagesByNameDesc()

    val allShortagesByImportance: Flow<List<Shortage>> = shortageDao.getShortagesByImportance()

    val allShortagesByType: Flow<List<Shortage>> = shortageDao.getShortagesByType()

    val getCount: Flow<Long> = shortageDao.getShortageCount()


    @WorkerThread
    suspend fun insert(shortage: Shortage) {
        shortageDao.insertShortage(shortage)
    }

    @WorkerThread
    suspend fun update(shortage: Shortage) {
        shortageDao.updateShortage(shortage)
    }

    @WorkerThread
    suspend fun delete(shortage: Shortage) {
        shortageDao.deleteShortage(shortage)
    }
}