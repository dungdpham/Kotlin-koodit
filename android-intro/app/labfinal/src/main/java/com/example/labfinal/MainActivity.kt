package com.example.labfinal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.ui.Modifier
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.labfinal.ui.navigation.NavigationController
import com.example.labfinal.ui.theme.AndroidIntroTheme
import com.example.labfinal.worker.DBUpdateWorker
import java.util.concurrent.TimeUnit

// Dung Pham 2217963 12.10.24
// MainActivity serves as the entry point for the application, where the user interface is set up
// and managed. Also schedules periodic database updates using WorkManager
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dbUpdateScheduler()

        enableEdgeToEdge()
        setContent {
            AndroidIntroTheme {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .systemBarsPadding()
                ) {
                    NavigationController()
                }
            }
        }
    }

    // responsible for setting up a periodic work request that triggers the DBUpdateWorker every 8 hours.
    // uses PeriodicWorkRequestBuilder to create a request that requires the device to be connected
    // to the network before execution.
    // enqueueUniquePeriodicWork ensures that the scheduled work is unique and updates any existing work named "DBUpdate".
    private fun dbUpdateScheduler() {
        val workRequest = PeriodicWorkRequestBuilder<DBUpdateWorker>(8, TimeUnit.HOURS)
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            )
            .build()

        WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork(
            "DBUpdate",
            ExistingPeriodicWorkPolicy.UPDATE,
            workRequest
        )
    }
}
