package com.example.simpleweather.network.openweather.rawmodel


import com.google.gson.annotations.SerializedName


data class RawOneCallResponse (
    @SerializedName("lat")
    val lat: Float? = null,

    @SerializedName("lon")
    val lon: Float? = null,

    @SerializedName("timezone")
    val timezone: String? = null,

    @SerializedName("timezone_offset")
    val timezoneOffset: Int? = null,

    @SerializedName("current")
    val current: RawCurrent = RawCurrent(),

    @SerializedName("hourly")
    val hourly: List<RawHourly> = ArrayList(),

    @SerializedName("daily")
    val daily: List<RawDaily> = ArrayList(),

    @SerializedName("alerts")
    val alerts: List<RawAlert> = ArrayList()
)
