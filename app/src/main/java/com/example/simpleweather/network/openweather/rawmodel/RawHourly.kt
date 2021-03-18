package com.example.simpleweather.network.openweather.rawmodel

import com.example.simpleweather.repository.model.HourlyWeatherCondition
import com.google.gson.annotations.SerializedName


data class RawHourly (
    @SerializedName("dt")
    val dt: Int,

    @SerializedName("temp")
    val temp: Float,

    @SerializedName("feels_like")
    val feelsLike: Float,

    @SerializedName("pressure")
    val pressure: Int,

    @SerializedName("humidity")
    val humidity: Int,

    @SerializedName("dew_point")
    val dewPoint: Float,

    @SerializedName("clouds")
    val clouds: Int,

    @SerializedName("visibility")
    val visibility: Int,

    @SerializedName("wind_speed")
    val windSpeed: Float,

    @SerializedName("wind_deg")
    val windDeg: Int,

    @SerializedName("weather")
    val weather: List<RawWeather>,

    @SerializedName("pop")
    val pop: Float,

    @SerializedName("snow")
    val snow: RawSnow? = null,

    @SerializedName("rain")
    val rain: RawRain? = null
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
            snow?.last_hour ?: 0F,
            rain?.last_hour ?: 0F
        )
    }

}