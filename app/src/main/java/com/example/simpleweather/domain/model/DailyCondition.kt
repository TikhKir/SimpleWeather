package com.example.simpleweather.domain.model

import com.example.simpleweather.data.local.model.DailyConditionDB
import com.example.simpleweather.utils.diffutil.Identified

//do not remove or change the default units initialization!
data class DailyCondition(

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

    fun toDailyConditionDB(locationId: Long): DailyConditionDB {
        return DailyConditionDB(
            locationParentId = locationId,
            timeStamp = timeStamp,
            timeZoneOffset = timeZoneOffset,
            sunrise = sunrise,
            sunset = sunset,
            tempDay = tempDay.toFloat(),
            tempEvening = tempEvening.toFloat(),
            tempNight = tempNight.toFloat(),
            tempMorning = tempMorning.toFloat(),
            tempMax = tempMax.toFloat(),
            tempMin = tempMin.toFloat(),
            tempDayFL = tempDayFL.toFloat(),
            tempEveningFL = tempEveningFL.toFloat(),
            tempNightFL = tempNightFL.toFloat(),
            tempMorningFL = tempMorningFL.toFloat(),
            pressure = pressure,
            humidity = humidity,
            dewPoint = dewPoint,
            clouds = clouds,
            windSpeed = windSpeed,
            windDeg = windDeg,
            weatherId = weatherId,
            weatherName = weatherName,
            weatherDescription = weatherDescription,
            weatherIcon = weatherIcon,
            probabilityOfPrecipitation = probabilityOfPrecipitation,
            snowVolume = snowVolume,
            rainVolume = rainVolume,
            uvi = uvi
        )
    }
}
