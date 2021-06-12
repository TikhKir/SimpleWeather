package com.example.simpleweather.network.openweather.rawmodel

import com.example.simpleweather.domain.model.DailyCondition
import com.google.gson.annotations.SerializedName
import kotlin.math.roundToInt


data class RawDaily (
    @SerializedName("dt")
    val dt: Int,

    @SerializedName("sunrise")
    val sunrise: Int,

    @SerializedName("sunset")
    val sunset: Int,

    @SerializedName("temp")
    val temp: RawTemp,

    @SerializedName("feels_like")
    val feelsLike: RawFeelsLike,

    @SerializedName("pressure")
    val pressure: Int,

    @SerializedName("humidity")
    val humidity: Int,

    @SerializedName("dew_point")
    val dewPoint: Float,

    @SerializedName("wind_speed")
    val windSpeed: Float,

    @SerializedName("wind_deg")
    val windDeg: Int,

    @SerializedName("weather")
    val weather: List<RawWeather>,

    @SerializedName("clouds")
    val clouds: Int,

    @SerializedName("pop")
    val pop: Float,

    @SerializedName("rain")
    val rain: Float,

    @SerializedName("uvi")
    val uvi: Float,

    @SerializedName("snow")
    val snow: Float
)

{

    fun toDailyCondition(): DailyCondition {
        return DailyCondition(
            timeStamp = dt,
            timeZoneOffset = 0,
            sunrise = sunrise,
            sunset = sunset,
            tempDay = temp.day.roundToInt(),
            tempEvening = temp.eve.roundToInt(),
            tempNight = temp.night.roundToInt(),
            tempMorning = temp.morn.roundToInt(),
            tempMax = temp.max.roundToInt(),
            tempMin = temp.min.roundToInt(),
            tempDayFL = feelsLike.day.roundToInt(),
            tempEveningFL = feelsLike.eve.roundToInt(),
            tempNightFL = feelsLike.night.roundToInt(),
            tempMorningFL = feelsLike.morn.roundToInt(),
            pressure = pressure,
            humidity = humidity,
            dewPoint = dewPoint,
            clouds = clouds,
            windSpeed = windSpeed,
            windDeg = windDeg,
            weatherId = weather[0].id,
            weatherName = weather[0].main,
            weatherDescription = weather[0].description,
            weatherIcon = weather[0].icon,
            probabilityOfPrecipitation = pop,
            snowVolume = snow,
            rainVolume = rain,
            uvi = uvi,
        )
    }

}