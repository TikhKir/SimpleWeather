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


    override suspend fun getAllForecastByCoord(lat: Float, lon: Float): List<DailyWeatherCondition> {
        return openWeatherService.getAllForecastByCoord(lat, lon)
            .daily
            .map { it.toDailyWeatherCondition() }
    }

    override suspend fun getDailyCondition(lat: Float, lon: Float): Result<List<DailyWeatherCondition>> {
        return wrapResponse {
            val rawResponse = openWeatherService.getDailyForecastByCoord(lat, lon)
            val offset = rawResponse.timezoneOffset
            rawResponse.daily
                .map { it.toDailyWeatherCondition() }
                .onEach { it.timeZoneOffset = offset }
        }
    }

    override suspend fun getHourlyCondition(lat: Float, lon: Float): Result<List<HourlyWeatherCondition>> {
        return wrapResponse {
            val rawResponse = openWeatherService.getHourlyForecastByCoord(lat, lon)
            val offset = rawResponse.timezoneOffset
            rawResponse.hourly
                .map { it.toHourlyWeatherCondition() }
                .onEach { it.timeZoneOffset = offset }
        }
    }

    override suspend fun getCurrentCondition(lat: Float, lon: Float): Result<CurrentWeatherCondition> {
        return wrapResponse {
            val rawResponse = openWeatherService.getCurrentlyForecastByCoord(lat, lon)
            val offset = rawResponse.timezoneOffset
            rawResponse.current.toCurrentWeatherCondition()
                .also { it.timeZoneOffset = offset }
        }
    }

    private suspend fun <T> wrapResponse(coroutine: suspend () -> T): Result<T> {
        return withContext(Dispatchers.IO) {
            try {
                Result.success(coroutine.invoke())
            } catch (e: Exception) {
                Result.error(e)
            }
        }
    }
}
