package com.example.simpleweather.network.openweather.rawmodel

import com.example.simpleweather.repository.model.HourlyWeatherCondition
import com.google.gson.annotations.SerializedName


data class RawHourly (
    @SerializedName("dt")
    val dt: Int,

    @SerializedName("temp")
    val temp: Float? = null,

    @SerializedName("feels_like")
    val feelsLike: Float? = null,

    @SerializedName("pressure")
    val pressure: Int? = null,

    @SerializedName("humidity")
    val humidity: Int? = null,

    @SerializedName("dew_point")
    val dewPoint: Float? = null,

    @SerializedName("clouds")
    val clouds: Int? = null,

    @SerializedName("visibility")
    val visibility: Int? = null,

    @SerializedName("wind_speed")
    val windSpeed: Float? = null,

    @SerializedName("wind_deg")
    val windDeg: Int? = null,

    @SerializedName("weather")
    val weather: List<RawWeather> = ArrayList(),

    @SerializedName("pop")
    val pop: Float? = null,

    @SerializedName("snow")
    val snow: Float? = null,

    @SerializedName("rain")
    val rain: Float? = null
)

{

    fun toHourlyWeatherCondition(): HourlyWeatherCondition {
        return HourlyWeatherCondition(
            dt,
            0,
            temp,
            feelsLike,
            pressure,
            humidity,
            windSpeed,
            windDeg,
            weather[0].id,
            weather[0].main,
            weather[0].description,
            weather[0].icon,
            pop,
            snow,
            rain
        )
    }

}