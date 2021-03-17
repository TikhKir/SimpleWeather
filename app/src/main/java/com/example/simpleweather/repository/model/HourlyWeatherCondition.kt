package com.example.simpleweather.repository.model

import com.example.simpleweather.local.model.HourlyWeatherConditionDB
import com.example.simpleweather.ui.model.HourlyConditionUI
import com.example.simpleweather.utils.diffutil.Identified
import kotlin.math.roundToInt

data class HourlyWeatherCondition(
    val timeStamp: Int,
    var timeZoneOffset: Int,

    val temp: Float,
    val tempFeelsLike: Float,
    val pressure: Int,
    val humidity: Int,
    val windSpeed: Float,
    val windDeg: Int,

    val weatherId: Int,
    val weatherName: String,
    val weatherDescription: String,
    val weatherIcon: String,

    val probabilityOfPrecipitation: Float,
    val snowVolume: Float,
    val rainVolume: Float
) : Identified
{
    override val identifier: Any = timeStamp

    fun toHourlyWeatherConditionDB(locationId: Long): HourlyWeatherConditionDB {
        return HourlyWeatherConditionDB(
            locationId.toInt(),
            timeStamp,
            timeZoneOffset,
            temp,
            tempFeelsLike,
            pressure,
            humidity,
            windSpeed,
            windDeg,
            weatherId,
            weatherName,
            weatherDescription,
            weatherIcon,
            probabilityOfPrecipitation,
            snowVolume,
            rainVolume
        )
    }

    fun toHourlyWeatherUI(): HourlyConditionUI {
        return HourlyConditionUI(
            timeStamp = timeStamp,
            timeZoneOffset = timeZoneOffset,
            temp = temp.roundToInt(),
            tempFL = tempFeelsLike.roundToInt(),
            pressure = pressure,
            humidity = humidity,
            windSpeed = windSpeed,
            windDeg = windDeg,
            weatherId = weatherId,
            weatherName = weatherName,
            weatherDescription = weatherDescription,
            weatherIcon = weatherIcon,
            probabilityOfPrecipitation = probabilityOfPrecipitation,
            snowVolume = snowVolume,
            rainVolume = rainVolume
        )
    }
}