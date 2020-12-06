package com.example.simpleweather.repository.model

import com.example.simpleweather.local.model.DailyWeatherConditionDB
import com.example.simpleweather.utils.diffutil.Identified

data class DailyWeatherCondition (
    val timeStamp: Int,
    var timeZoneOffset: Int,

    val sunrise: Int?,
    val sunset: Int?,

    val tempDay: Float?,
    val tempEvening: Float?,
    val tempNight: Float?,
    val tempMorning: Float?,
    val tempMax: Float?,
    val tempMin: Float?,

    val tempDayFL: Float?,
    val tempEveningFL: Float?,
    val tempNightFL: Float?,
    val tempMorningFL: Float?,

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

    val probabilityOfPrecipitation: Float?,
    val snowVolume: Float?,
    val rainVolume: Float?,
    val uvi: Float?
) : Identified
{
    override val identifier: Any = timeStamp

    fun toDailyWeatherConditionDB(locationId: Long): DailyWeatherConditionDB {
        return DailyWeatherConditionDB(
            locationId,
            timeStamp,
            timeZoneOffset,
            sunrise,
            sunset,
            tempDay,
            tempEvening,
            tempNight,
            tempMorning,
            tempMax,
            tempMin,
            tempDayFL,
            tempEveningFL,
            tempNightFL,
            tempMorningFL,
            pressure,
            humidity,
            dewPoint,
            clouds,
            windSpeed,
            windDeg,
            weatherId,
            weatherName,
            weatherDescription,
            weatherIcon,
            probabilityOfPrecipitation,
            snowVolume,
            rainVolume,
            uvi
        )
    }

}