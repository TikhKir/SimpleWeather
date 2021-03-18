package com.example.simpleweather.network.openweather.rawmodel


import com.google.gson.annotations.SerializedName


data class RawOneCallResponse (
    @SerializedName("lat")
    val lat: Float,

    @SerializedName("lon")
    val lon: Float,

    @SerializedName("timezone")
    val timezone: String,

    @SerializedName("timezone_offset")
    var timezoneOffset: Int = 0,

    @SerializedName("current")
    val current: RawCurrent,

    @SerializedName("hourly")
    val hourly: List<RawHourly>,

    @SerializedName("daily")
    val daily: List<RawDaily>,

    @SerializedName("alerts")
    val alerts: List<RawAlert>
)
