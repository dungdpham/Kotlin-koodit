package com.example.labfinal.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.labfinal.data.database.MPDatabase
import com.example.labfinal.data.repository.MPRepository
import java.io.IOException

// Dung Pham 2217963 11.10.24
// Uses WorkManager to perform background tasks of fetching data from remote and insert/update the
// data to local database. WorkManager can schedule and execute asynchronous tasks even if the
// app is not active.
class DBUpdateWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    // lazy initialization of the repository
    private val repository: MPRepository by lazy {
        val database = MPDatabase.getDatabase()
        MPRepository(
            mpDataDao = database.mpDataDao(),
            mpDataExtraDao = database.mpDataExtraDao(),
            mpGradingDao = database.mpGradingDao()
        )
    }

    // WorkManager task to fetch data from remote source and update the database
    override suspend fun doWork(): Result {
        Log.d("DBG", "DBUpdateWorker started")
        return try {
            repository.fetchFromRemote()
            Log.d("DBG", "DBUpdateWorker finished")
            Result.success()
        } catch (e: IOException) {
            Log.e("DBG", "Network error while fetching from remote: $e")
            Result.retry() // retry the work if encounter network error
        } catch (e: Exception) {
            Log.e("DBG", "Other error while fetching from remote: $e")
            Result.failure() // fail the work for other unexpected error
        }
    }
}