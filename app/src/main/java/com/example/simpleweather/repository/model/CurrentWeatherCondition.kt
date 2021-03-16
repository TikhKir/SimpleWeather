package com.example.simpleweather.repository.model

import com.example.simpleweather.ui.model.CurrentConditionUI
import com.example.simpleweather.utils.diffutil.Identified
import kotlin.math.roundToInt

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
            sunrise,
            sunset,
            temp = temp?.roundToInt().toString(),
            tempFL = tempFL?.roundToInt().toString(),
            pressure = pressure.toString(),
            humidity = humidity.toString(),
            dewPoint = dewPoint.toString(),
            clouds = clouds.toString(),
            windSpeed = windSpeed.toString(),
            windDeg = windDeg.toString(),
            weatherId = weatherId,
            weatherName = weatherName ?: "",
            weatherDescription = weatherDescription.toString(),
            weatherIcon = weatherIcon ?: "",
            snowVolumeLastHour = snowVolumeLastHour.toString(),
            rainVolumeLastHour = rainVolumeLastHour.toString(),
            allVolumeLastHour = ((snowVolumeLastHour ?: 0F) + (rainVolumeLastHour ?: 0F)).toString(),
            uvi = uvi.toString()
        )
    }
}