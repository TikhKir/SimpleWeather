package com.example.simpleweather

import android.app.Application
import androidx.work.*
import com.example.simpleweather.utils.worker.BackgroundUpdateWorker
import com.jakewharton.threetenabp.AndroidThreeTen
import dagger.hilt.android.HiltAndroidApp
import java.util.concurrent.TimeUnit

@HiltAndroidApp
class SimpleWeatherApplication: Application() {

    private lateinit var workManager : WorkManager

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)

        workManager = WorkManager.getInstance(this)
        doBackgroundRefresh()
    }

        private fun doBackgroundRefresh() {
        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val periodicRequest = PeriodicWorkRequest
            .Builder(BackgroundUpdateWorker::class.java, 16, TimeUnit.MINUTES)
            .setConstraints(constraints)
            .build()

        workManager.enqueueUniquePeriodicWork(
            "REFRESH WORK", //todo: hardcode
            ExistingPeriodicWorkPolicy.KEEP,
            periodicRequest
        )
    }
}