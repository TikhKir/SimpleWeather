package com.example.simpleweather.network.openweather.rawmodel

import com.example.simpleweather.domain.model.CurrentCondition
import com.google.gson.annotations.SerializedName
import kotlin.math.roundToInt


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

    fun toCurrentCondition(): CurrentCondition {
        return CurrentCondition(
            timeStamp = dt,
            timeZoneOffset = 0,
            sunrise = sunrise,
            sunset = sunset,
            temp = temp.roundToInt(),
            tempFL = feelsLike.roundToInt(),
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
            snowVolumeLastHour = snow?.last_hour ?: 0F,
            rainVolumeLastHour = rain?.last_hour ?: 0F,
            allVolumeLastHour = (snow?.last_hour ?: 0F) + (rain?.last_hour ?: 0F),
            uvi = uvi
        )
    }
}