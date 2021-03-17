package com.example.simpleweather.network.openweather.rawmodel

import com.example.simpleweather.repository.model.CurrentWeatherCondition
import com.google.gson.annotations.SerializedName



data class RawCurrent (
    @SerializedName("dt")
    val dt: Int,

    @SerializedName("sunrise")
    val sunrise: Int,

    @SerializedName("sunset")
    val sunset: Int,

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

    @SerializedName("wind_speed")
    val windSpeed: Float,

    @SerializedName("wind_deg")
    val windDeg: Int,

    @SerializedName("weather")
    val weather: List<RawWeather>,

    @SerializedName("clouds")
    val clouds: Int,

    @SerializedName("visibility")
    val visibility: Int,

    @SerializedName("uvi")
    val uvi: Float,

    @SerializedName("rain")
    val rain: RawRain? = null,

    @SerializedName("snow")
    val snow: RawSnow? = null
) {
    fun toCurrentWeatherCondition(): CurrentWeatherCondition {
        return CurrentWeatherCondition(
            dt,
            0,
            sunrise,
            sunset,
            temp,
            feelsLike,
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
            snow?.last_hour ?: 0F,
            rain?.last_hour ?: 0F,
            uvi
        )
    }
}