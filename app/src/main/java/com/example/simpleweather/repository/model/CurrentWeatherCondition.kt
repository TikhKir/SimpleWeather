package com.example.simpleweather.repository.model

import com.example.simpleweather.ui.model.CurrentConditionUI
import kotlin.math.roundToInt

data class CurrentWeatherCondition (
    val timeStamp: Int,
    var timeZoneOffset: Int,

    val sunrise: Int,
    val sunset: Int,

    var temp: Float,
    val tempFL: Float,

    var pressure: Int,
    val humidity: Int,
    val dewPoint: Float,
    val clouds: Int,
    var windSpeed: Float,
    val windDeg: Int,

    val weatherId: Int,
    val weatherName: String,
    val weatherDescription: String,
    val weatherIcon: String,

    val snowVolumeLastHour: Float,
    val rainVolumeLastHour: Float,
    val uvi: Float
)
{


    fun toCurrentConditionUI(): CurrentConditionUI {
        return CurrentConditionUI(
            timeStamp,
            timeZoneOffset,
            sunrise,
            sunset,
            temp = temp.roundToInt(),
            tempFL = tempFL.roundToInt(),
            pressure = pressure,
            humidity = humidity,
            dewPoint = dewPoint,
            clouds = clouds,
            windSpeed = windSpeed,
            windDeg = windDeg,
            weatherId = weatherId,
            weatherName = weatherName,
            weatherDescription = weatherDescription,
            weatherIcon = weatherIcon,
            snowVolumeLastHour = snowVolumeLastHour,
            rainVolumeLastHour = rainVolumeLastHour,
            allVolumeLastHour = (snowVolumeLastHour + rainVolumeLastHour),
            uvi = uvi
        )
    }

}