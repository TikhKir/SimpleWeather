package com.example.simpleweather.network.openweather

import com.example.simpleweather.domain.model.CurrentCondition
import com.example.simpleweather.domain.model.DailyCondition
import com.example.simpleweather.domain.model.HourlyCondition
import com.example.simpleweather.utils.datawrappers.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class OpenWeatherApiImpl @Inject constructor(
    private val openWeatherService: OpenWeatherService
) : OpenWeatherApi {

    override suspend fun getDailyCondition(lat: Float, lon: Float): Result<List<DailyCondition>> {
        return wrapResponse {
            val rawResponse = openWeatherService.getDailyForecastByCoord(lat, lon)
            val offset = rawResponse.timezoneOffset
            rawResponse.daily
                .map { it.toDailyCondition() }
                .onEach { it.timeZoneOffset = offset }
        }
    }

    override suspend fun getHourlyCondition(lat: Float, lon: Float): Result<List<HourlyCondition>> {
        return wrapResponse {
            val rawResponse = openWeatherService.getHourlyForecastByCoord(lat, lon)
            val offset = rawResponse.timezoneOffset
            rawResponse.hourly
                .map { it.toHourlyCondition() }
                .onEach { it.timeZoneOffset = offset }
        }
    }

    override suspend fun getCurrentCondition(lat: Float, lon: Float): Result<CurrentCondition> {
        return wrapResponse {
            val rawResponse = openWeatherService.getCurrentlyForecastByCoord(lat, lon)
            val offset = rawResponse.timezoneOffset
            rawResponse.current.toCurrentCondition()
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
