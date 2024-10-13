package com.example.labfinal.data.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.labfinal.MPApp
import com.example.labfinal.data.dao.MPDataDao
import com.example.labfinal.data.dao.MPDataExtraDao
import com.example.labfinal.data.dao.MPGradingDao
import com.example.labfinal.data.model.MPData
import com.example.labfinal.data.model.MPDataExtra
import com.example.labfinal.data.model.MPGrading

// Dung Pham 2217963 10.10.24
// Database for the application. Provides singleton instance for database access
// and abstract method to obtain DAOs
@Database(entities = [MPData::class, MPDataExtra::class, MPGrading::class], version = 1, exportSchema = false)
abstract class MPDatabase : RoomDatabase() {
    abstract fun mpDataDao(): MPDataDao
    abstract fun mpDataExtraDao(): MPDataExtraDao
    abstract fun mpGradingDao(): MPGradingDao

    companion object {
        private var Instance: MPDatabase? = null

        fun getDatabase(): MPDatabase {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(MPApp.appContext, MPDatabase::class.java, "mp_database")
                    .build()
                    .also { Instance = it }
            }
        }
    }
}