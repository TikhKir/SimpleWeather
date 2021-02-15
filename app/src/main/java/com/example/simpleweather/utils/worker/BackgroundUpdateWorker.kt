package com.example.simpleweather.utils.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class BackgroundUpdateWorker(context: Context, params: WorkerParameters)
    : CoroutineWorker(context, params)  {

    override suspend fun doWork(): Result {
        Log.e("BACKGROUND WORKER", "I DO!" )
        return Result.success()
    }

}