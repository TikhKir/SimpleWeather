package com.example.simpleweather.ui.model

import com.example.simpleweather.utils.diffutil.Identified

data class CurrentConditionUI(
    val timeStamp: Int,
    val timeZoneOffset: Int,

    var sunrise: Int = 0,
    var sunset: Int = 0,

    var temp: Int = 0,
    var tempFL: Int = 0,
    var tempUnits: DegreeUnits = DegreeUnits.Celsius,

    var pressure: Int = 0,
    var pressureUnits: PressureUnits = PressureUnits.HectoPascals,
    var humidity: Int = 0,
    var dewPoint: Float = 0F,
    var clouds: Int = 0,
    var windSpeed: Float = 0F,
    var windSpeedUnits: WindSpeedUnits = WindSpeedUnits.MetersPerSecond,
    var windDeg: Int = 0,

    var weatherId: Int = 0,
    var weatherName: String = "",
    var weatherDescription: String = "",
    var weatherIcon: String = "",

    var snowVolumeLastHour: Float = 0F,
    var rainVolumeLastHour: Float = 0F,
    var allVolumeLastHour: Float = 0F,
    var uvi: Float = 0F
) : Identified
{
    override val identifier: Any = timeStamp
}