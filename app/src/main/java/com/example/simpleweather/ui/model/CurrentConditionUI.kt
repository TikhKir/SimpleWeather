package com.example.simpleweather.ui.model

import com.example.simpleweather.utils.diffutil.Identified

data class CurrentConditionUI(
    val timeStamp: Int,
    val timeZoneOffset: Int,

    var sunrise: Int?,
    var sunset: Int?,

    var temp: String = "",
    var tempFL: String = "",

    var pressure: String = "",
    var humidity: String = "",
    var dewPoint: String = "",
    var clouds: String = "",
    var windSpeed: String = "",
    var windDeg: String = "",

    var weatherId: Int?,
    var weatherName: String = "",
    var weatherDescription: String = "",
    var weatherIcon: String = "",

    var snowVolumeLastHour: String = "",
    var rainVolumeLastHour: String = "",
    var allVolumeLastHour: String = "",
    var uvi: String = ""
) : Identified
{
    override val identifier: Any = timeStamp
}