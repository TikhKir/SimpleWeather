package com.example.simpleweather.network.openweather

import com.example.simpleweather.repository.model.CurrentWeatherCondition
import com.example.simpleweather.repository.model.DailyWeatherCondition
import com.example.simpleweather.repository.model.HourlyWeatherCondition
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

    override suspend fun getHourlyCondition(lat: Float, lon: Float): List<HourlyWeatherCondition> {
        return openWeatherService.getHourlyForecastByCoord(lat, lon)
            .hourly
            .map { it.toHourlyWeatherCondition() }
    }

    override suspend fun getCurrentCondition(lat: Float, lon: Float): CurrentWeatherCondition {
        return openWeatherService.getCurrentlyForecastByCoord(lat, lon)
            .current
            .toCurrentWeatherCondition()

    }


}
