package com.example.simpleweather.network.openweather.rawmodel

import com.example.simpleweather.repository.model.CurrentWeatherCondition
import com.google.gson.annotations.SerializedName



data class RawCurrent (
    @SerializedName("dt")
    val dt: Int? = null,

    @SerializedName("sunrise")
    val sunrise: Int? = null,

    @SerializedName("sunset")
    val sunset: Int? = null,

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

    @SerializedName("wind_speed")
    val windSpeed: Float? = null,

    @SerializedName("wind_deg")
    val windDeg: Int? = null,

    @SerializedName("weather")
    val weather: List<RawWeather> = ArrayList(),

    @SerializedName("clouds")
    val clouds: Int? = null,

    @SerializedName("visibility")
    val visibility: Int? = null,

    @SerializedName("uvi")
    val uvi: Float? = null,

    @SerializedName("rain")
    val rain: RawRain? = null,

    @SerializedName("snow")
    val snow: RawSnow? = null
) {
    fun toCurrentWeatherCondition(): CurrentWeatherCondition {
        return CurrentWeatherCondition(
            dt!!,
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
            snow?.last_hour,
            rain?.last_hour,
            uvi
        )
    }
}