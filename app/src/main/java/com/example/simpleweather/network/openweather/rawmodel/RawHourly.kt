package com.example.simpleweather.network.openweather.rawmodel

import com.example.simpleweather.domain.model.HourlyCondition
import com.google.gson.annotations.SerializedName
import kotlin.math.roundToInt


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

    fun toHourlyCondition(): HourlyCondition {
        return HourlyCondition(
            timeStamp = dt,
            timeZoneOffset = 0,
            temp = temp.roundToInt(),
            tempFL = feelsLike.roundToInt(),
            pressure = pressure,
            humidity = humidity,
            windSpeed = windSpeed,
            windDeg = windDeg,
            weatherId = weather[0].id,
            weatherDescription = weather[0].description,
            weatherName = weather[0].main,
            weatherIcon = weather[0].icon,
            probabilityOfPrecipitation = pop,
            snowVolume = snow?.last_hour ?: 0F,
            rainVolume = rain?.last_hour ?: 0F
        )
    }

}