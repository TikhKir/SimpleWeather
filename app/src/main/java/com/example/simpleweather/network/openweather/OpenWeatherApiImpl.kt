package com.example.simpleweather.network.openweather

import com.example.simpleweather.repository.model.CurrentWeatherCondition
import com.example.simpleweather.repository.model.DailyWeatherCondition
import com.example.simpleweather.repository.model.HourlyWeatherCondition
import com.example.simpleweather.utils.datawrappers.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class OpenWeatherApiImpl @Inject constructor(
    private val openWeatherService: OpenWeatherService
) : OpenWeatherApi {


    override suspend fun getAllForecastByCoord(lat: Float, lon: Float
    ): List<DailyWeatherCondition> {
        return openWeatherService.getAllForecastByCoord(lat, lon)
            .daily
            .map { it.toDailyWeatherCondition() }
    }

    override suspend fun getDailyCondition(lat: Float, lon: Float): List<DailyWeatherCondition> {
        return openWeatherService.getDailyForecastByCoord(lat, lon)
            .daily
            .map { it.toDailyWeatherCondition() }
    }

    override suspend fun getHourlyCondition(lat: Float, lon: Float): Result<List<HourlyWeatherCondition>> {
        return handleResponse {
            openWeatherService.getHourlyForecastByCoord(lat, lon)
                .hourly
                .map { it.toHourlyWeatherCondition() }
        }
    }

    override suspend fun getCurrentCondition(lat: Float, lon: Float): CurrentWeatherCondition {
        return openWeatherService.getCurrentlyForecastByCoord(lat, lon)
            .current
            .toCurrentWeatherCondition()

    }

    private suspend fun <T> handleResponse(coroutine: suspend () -> T): Result<T> {
        return  withContext(Dispatchers.IO) {
            try {
                Result.success(coroutine.invoke())
            } catch (e: Exception) {
                Result.error(e)
            }
        }
    }
}
