package com.example.simpleweather.repository.model

import com.example.simpleweather.utils.diffutil.Identified

data class CurrentWeatherCondition (
    val timeStamp: Int,
    var timeZoneOffset: Int,

    val sunrise: Int?,
    val sunset: Int?,

    val temp: Float?,
    val tempFL: Float?,

    val pressure: Int?,
    val humidity: Int?,
    val dewPoint: Float?,
    val clouds: Int?,
    val windSpeed: Float?,
    val windDeg: Int?,

    val weatherId: Int?,
    val weatherName: String?,
    val weatherDescription: String?,
    val weatherIcon: String?,

    val snowVolumeLastHour: Float?,
    val rainVolumeLastHour: Float?,
    val uvi: Float?
) : Identified
{
    override val identifier: Any = timeStamp
}