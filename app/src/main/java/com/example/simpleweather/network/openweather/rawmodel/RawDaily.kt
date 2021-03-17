package com.example.simpleweather.network.openweather.rawmodel

import com.example.simpleweather.repository.model.DailyWeatherCondition
import com.google.gson.annotations.SerializedName


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

    fun toDailyWeatherCondition(): DailyWeatherCondition {
        return DailyWeatherCondition(
            dt,
            0,
            sunrise,
            sunset,
            temp.day,
            temp.eve,
            temp.night,
            temp.morn,
            temp.max,
            temp.min,
            feelsLike.day,
            feelsLike.eve,
            feelsLike.night,
            feelsLike.morn,
            pressure,
            humidity,
            dewPoint,
            clouds,
            windSpeed,
            windDeg,
            weather[0].id,
            weather[0].main,
            weather[0].description,
            weather[0].icon,
            pop,
            snow,
            rain,
            uvi
        )
    }

}