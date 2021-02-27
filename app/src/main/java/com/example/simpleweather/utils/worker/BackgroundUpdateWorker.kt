package com.example.simpleweather.utils.worker

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.simpleweather.local.DataApi
import com.example.simpleweather.network.openweather.OpenWeatherApi
import com.example.simpleweather.repository.model.LocationWithCoords
import com.example.simpleweather.utils.Constants
import com.example.simpleweather.utils.datawrappers.ResultType
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import org.threeten.bp.Instant

@HiltWorker
class BackgroundUpdateWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val dataApi: DataApi,
    private val openWeatherApi: OpenWeatherApi
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        Log.e("BACKGROUND WORKER", "I DO!")
        val locations = dataApi.getSavedLocationsSync()

        for (location in locations) {
            refreshHourlyConditions(location)
            refreshDailyConditions(location)
        }
        return Result.success()
        //todo: нужно добавить регистацию неудачных попыток обновления через Result.failure()
    }


    private suspend fun refreshHourlyConditions(location: LocationWithCoords) {
        val currentTime = Instant.now().epochSecond
        val timeDifference = currentTime - location.refreshTimeHourly

        if (timeDifference > Constants.REFRESH_INTERVAL) {
            val netResponse =
                openWeatherApi.getHourlyCondition(location.latitude, location.longitude)
            if (netResponse.resultType == ResultType.SUCCESS) {
                netResponse.data?.let {
                    dataApi.saveHourlyForecast(location.locationId, it)
                    dataApi.updateHourlyRefreshTime(currentTime, location.locationId)
                    Log.e("BACKGROUND_UPDATE", "Refresh hourly done")
                }
            }
        } else {
            Log.e("BACKGROUND_UPDATE_FAIL", "Refresh interval is not over")
        }
    }

    private suspend fun refreshDailyConditions(location: LocationWithCoords) {
        val currentTime = Instant.now().epochSecond
        val timeDifference = currentTime - location.refreshTimeDaily

        if (timeDifference > Constants.REFRESH_INTERVAL) {
            val netResponse =
                openWeatherApi.getDailyCondition(location.latitude, location.longitude)
            if (netResponse.resultType == ResultType.SUCCESS) {
                netResponse.data?.let {
                    dataApi.updateDailyRefreshTime(currentTime, location.locationId)
                    dataApi.saveDailyForecast(location.locationId, it)
                    Log.e("BACKGROUND_UPDATE", "Refresh daily done")
                }
            }
        } else {
            Log.e("BACKGROUND_UPDATE_FAIL", "Refresh interval is not over")
        }
    }

}
