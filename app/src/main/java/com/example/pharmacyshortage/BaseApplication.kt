package com.example.pharmacyshortage

import android.app.Application
import com.example.pharmacyshortage.data.AppDatabase
import com.example.pharmacyshortage.data.ShortageRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

/**
 * An application class that inherits from [Application], allows for the creation of a singleton
 * instance of the [AppDatabase]
 */
class BaseApplication : Application() {
    private val applicationScope = CoroutineScope(SupervisorJob())

    private val database by lazy { AppDatabase.getDatabase(this, applicationScope) }
    val repository by lazy { ShortageRepository(database.shortageDao()) }
}