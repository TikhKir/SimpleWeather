package com.example.simpleweather.ui.model

import com.example.simpleweather.utils.asyncunits.DegreeUnits
import com.example.simpleweather.utils.asyncunits.PressureUnits
import com.example.simpleweather.utils.asyncunits.WindSpeedUnits
import com.example.simpleweather.utils.diffutil.Identified

//do not remove or change the default units initialization!
data class DailyConditionUI(

    val timeStamp: Int,
    var timeZoneOffset: Int,

    var sunrise: Int,
    var sunset: Int,

    var tempDay: Int,
    var tempEvening: Int,
    var tempNight: Int,
    var tempMorning: Int,
    var tempMax: Int,
    var tempMin: Int,

    var tempDayFL: Int,
    var tempEveningFL: Int,
    var tempNightFL: Int,
    var tempMorningFL: Int,
    var tempUnits: DegreeUnits = DegreeUnits.Celsius,

    var pressure: Int,
    var pressureUnits: PressureUnits = PressureUnits.HectoPascals,
    var humidity: Int,
    var dewPoint: Float,
    var clouds: Int,
    var windSpeed: Float,
    var windSpeedUnits: WindSpeedUnits = WindSpeedUnits.MetersPerSecond,
    var windDeg: Int,

    var weatherId: Int,
    var weatherName: String,
    var weatherDescription: String,
    var weatherIcon: String,

    var probabilityOfPrecipitation: Float,
    var snowVolume: Float,
    var rainVolume: Float,
    var uvi: Float,
) : Identified {
    override val identifier = timeStamp
}
