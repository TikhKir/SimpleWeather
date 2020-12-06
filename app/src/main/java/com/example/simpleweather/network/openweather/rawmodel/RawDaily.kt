package com.example.simpleweather.network.openweather.rawmodel

import com.example.simpleweather.repository.model.DailyWeatherCondition
import com.google.gson.annotations.SerializedName


data class RawDaily (
    @SerializedName("dt")
    val dt: Int,

    @SerializedName("sunrise")
    val sunrise: Int? = null,

    @SerializedName("sunset")
    val sunset: Int? = null,

    @SerializedName("temp")
    val temp: RawTemp? = null,

    @SerializedName("feels_like")
    val feelsLike: RawFeelsLike? = null,

    @SerializedName("pressure")
    val pressure: Int? = null,

    @SerializedName("humidity")
    val humidity: Int? = null,

    @SerializedName("dew_point")
    val dewPoint: Float? = null,

    @SerializedName("wind_speed")
    val windSpeed: Float? = null,

    @SerializedName("wind_deg")
    val windDeg: Int? = null,

    @SerializedName("weather")
    val weather: List<RawWeather> = ArrayList(),

    @SerializedName("clouds")
    val clouds: Int? = null,

    @SerializedName("pop")
    val pop: Float? = null,

    @SerializedName("rain")
    val rain: Float? = null,

    @SerializedName("uvi")
    val uvi: Float? = null,

    @SerializedName("snow")
    val snow: Float? = null
)

{

    fun toDailyWeatherCondition(): DailyWeatherCondition {
        return DailyWeatherCondition(
            dt,
            0,
            sunrise,
            sunset,
            temp?.day,
            temp?.eve,
            temp?.night,
            temp?.morn,
            temp?.max,
            temp?.min,
            feelsLike?.day,
            feelsLike?.eve,
            feelsLike?.night,
            feelsLike?.morn,
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