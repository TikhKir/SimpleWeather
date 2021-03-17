package com.example.simpleweather.ui.model

import com.example.simpleweather.utils.diffutil.Identified

data class HourlyConditionUI(
    val timeStamp: Int,
    var timeZoneOffset: Int,

    var temp: Int,
    var tempFL: Int,
    var tempUnits: DegreeUnits = DegreeUnits.Celsius,
    var pressure: Int,
    var pressureUnits: PressureUnits = PressureUnits.HectoPascals,
    var humidity: Int,
    var windSpeed: Float,
    var windSpeedUnits: WindSpeedUnits = WindSpeedUnits.MetersPerSecond,
    var windDeg: Int,

    var weatherId: Int,
    var weatherName: String,
    var weatherDescription: String,
    var weatherIcon: String,

    var probabilityOfPrecipitation: Float,
    var snowVolume: Float,
    var rainVolume: Float
) : Identified
{
    override val identifier: Any = timeStamp
}
