package com.example.simpleweather.repository.model

import com.example.simpleweather.ui.model.CurrentConditionUI
import com.example.simpleweather.utils.diffutil.Identified

data class CurrentWeatherCondition (
    val timeStamp: Int,
    var timeZoneOffset: Int,

    val sunrise: Int?,
    val sunset: Int?,

    var temp: Float?,
    val tempFL: Float?,

    var pressure: Int?,
    val humidity: Int?,
    val dewPoint: Float?,
    val clouds: Int?,
    var windSpeed: Float?,
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

    fun toCurrentConditionUI(): CurrentConditionUI {
        return CurrentConditionUI(
            timeStamp,
            timeZoneOffset,
            sunrise ?: 0,
            sunset ?: 0,
            temp = temp?.toInt() ?: 0,
            tempFL = tempFL?.toInt() ?: 0,
            pressure = pressure ?: 0,
            humidity = humidity ?: 0,
            dewPoint = dewPoint ?: 0F,
            clouds = clouds ?: 0,
            windSpeed = windSpeed ?: 0F,
            windDeg = windDeg ?: 0,
            weatherId = weatherId ?: 0,
            weatherName = weatherName ?: "",
            weatherDescription = weatherDescription ?: "",
            weatherIcon = weatherIcon ?: "",
            snowVolumeLastHour = snowVolumeLastHour ?: 0F,
            rainVolumeLastHour = rainVolumeLastHour ?: 0F,
            allVolumeLastHour = ((snowVolumeLastHour ?: 0F) + (rainVolumeLastHour ?: 0F)),
            uvi = uvi ?: 0F
        )
    }
}