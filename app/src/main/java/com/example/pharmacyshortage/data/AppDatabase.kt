package com.example.pharmacyshortage.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.pharmacyshortage.model.Shortage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Room database to persist data for the PharmacyShortage app.
 * This database stores a [Shortage] entity
 * Also has pre-populated data
 */
@Database(entities = [Shortage::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase(){

abstract fun shortageDao(): ShortageDao

    private class AppDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(database.shortageDao())
                }
            }
        }

        suspend fun populateDatabase(shortageDao: ShortageDao) {
            // Delete all content here.
            shortageDao.deleteAll()

            // Add sample words.
            var shortage = Shortage(0,"Aspirin",1500, listOf(""),true)
            shortageDao.insertShortage(shortage)
            shortage = Shortage(0,"Paracetamol",2000, listOf(""),true)
            shortageDao.insertShortage(shortage)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        val converter = Converters()
        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "shortage_database"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(AppDatabaseCallback(scope))
                    .addTypeConverter(converter)
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }

}
