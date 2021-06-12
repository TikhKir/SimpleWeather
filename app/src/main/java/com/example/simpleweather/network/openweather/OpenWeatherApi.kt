package com.example.simpleweather.network.openweather

import com.example.simpleweather.domain.model.CurrentCondition
import com.example.simpleweather.domain.model.DailyCondition
import com.example.simpleweather.domain.model.HourlyCondition
import com.example.simpleweather.utils.datawrappers.Result

interface OpenWeatherApi {

    suspend fun getDailyCondition(lat: Float, lon: Float): Result<List<DailyCondition>>

    suspend fun getHourlyCondition(lat: Float, lon: Float): Result<List<HourlyCondition>>

    suspend fun getCurrentCondition(lat: Float, lon: Float): Result<CurrentCondition>
}