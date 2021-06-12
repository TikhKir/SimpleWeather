package com.example.simpleweather.data.network.openweather

import com.example.simpleweather.domain.datawrappers.Result
import com.example.simpleweather.domain.model.CurrentCondition
import com.example.simpleweather.domain.model.DailyCondition
import com.example.simpleweather.domain.model.HourlyCondition

interface OpenWeatherApi {

    suspend fun getDailyCondition(lat: Float, lon: Float): Result<List<DailyCondition>>

    suspend fun getHourlyCondition(lat: Float, lon: Float): Result<List<HourlyCondition>>

    suspend fun getCurrentCondition(lat: Float, lon: Float): Result<CurrentCondition>
}