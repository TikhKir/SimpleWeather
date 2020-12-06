package com.example.simpleweather.network.openweather

import com.example.simpleweather.repository.model.CurrentWeatherCondition
import com.example.simpleweather.repository.model.DailyWeatherCondition
import com.example.simpleweather.repository.model.HourlyWeatherCondition
import com.example.simpleweather.utils.datawrappers.Result

interface OpenWeatherApi {

    suspend fun getAllForecastByCoord(lat: Float, lon: Float): List<DailyWeatherCondition>

    suspend fun getDailyCondition(lat: Float, lon: Float): List<DailyWeatherCondition>

    suspend fun getHourlyCondition(lat: Float, lon: Float): Result<List<HourlyWeatherCondition>>

    suspend fun getCurrentCondition(lat: Float, lon: Float): Result<CurrentWeatherCondition>
}