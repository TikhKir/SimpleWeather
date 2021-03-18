package com.example.simpleweather.repository.model

import com.example.simpleweather.local.model.DailyWeatherConditionDB
import com.example.simpleweather.ui.model.DailyConditionUI
import kotlin.math.roundToInt

data class DailyWeatherCondition (
    val timeStamp: Int,
    var timeZoneOffset: Int,

    val sunrise: Int,
    val sunset: Int,

    var tempDay: Float,
    val tempEvening: Float,
    val tempNight: Float,
    val tempMorning: Float,
    val tempMax: Float,
    val tempMin: Float,

    val tempDayFL: Float,
    val tempEveningFL: Float,
    val tempNightFL: Float,
    val tempMorningFL: Float,

    val pressure: Int,
    val humidity: Int,
    val dewPoint: Float,
    val clouds: Int,
    val windSpeed: Float,
    val windDeg: Int,

    val weatherId: Int,
    val weatherName: String,
    val weatherDescription: String,
    val weatherIcon: String,

    val probabilityOfPrecipitation: Float,
    val snowVolume: Float,
    val rainVolume: Float,
    val uvi: Float
)
{

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

    fun toDailyConditionUI(): DailyConditionUI {
        return DailyConditionUI(
            timeStamp = timeStamp,
            timeZoneOffset = timeZoneOffset,
            sunrise = sunrise,
            sunset = sunset,
            tempDay = tempDay.roundToInt(),
            tempEvening = tempEvening.roundToInt(),
            tempNight = tempNight.roundToInt(),
            tempMorning = tempMorning.roundToInt(),
            tempMax = tempMax.roundToInt(),
            tempMin = tempMin.roundToInt(),
            tempDayFL = tempDayFL.roundToInt(),
            tempEveningFL = tempEveningFL.roundToInt(),
            tempNightFL = tempNightFL.roundToInt(),
            tempMorningFL = tempMorningFL.roundToInt(),
            pressure = pressure,
            humidity = humidity,
            dewPoint = dewPoint,
            clouds = clouds,
            windSpeed = windSpeed,
            windDeg = windDeg,
            weatherId = weatherId,
            weatherName = weatherName,
            weatherIcon = weatherIcon,
            snowVolume = snowVolume,
            rainVolume = rainVolume,
            probabilityOfPrecipitation = probabilityOfPrecipitation,
            weatherDescription = weatherDescription,
            uvi = uvi
        )
    }

}