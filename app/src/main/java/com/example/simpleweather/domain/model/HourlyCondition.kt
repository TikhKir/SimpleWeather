package com.example.simpleweather.domain.model

import com.example.simpleweather.data.local.model.HourlyConditionDB
import com.example.simpleweather.utils.diffutil.Identified

//do not remove or change the default units initialization!
data class HourlyCondition(
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
) : Identified {
    override val identifier: Any = timeStamp

    fun toHourlyConditionDB(locationId: Long): HourlyConditionDB {
        return HourlyConditionDB(
            locationParentId = locationId.toInt(),
            timeStamp = timeStamp,
            timeZoneOffset = timeZoneOffset,
            temp = temp.toFloat(),
            tempFeelsLike = tempFL.toFloat(),
            pressure = pressure,
            humidity = humidity,
            windSpeed = windSpeed,
            windDeg = windDeg,
            weatherId = weatherId,
            weatherName = weatherName,
            weatherDescription = weatherDescription,
            weatherIcon = weatherIcon,
            probabilityOfPrecipitation = probabilityOfPrecipitation,
            snowVolume = snowVolume,
            rainVolume = rainVolume
        )
    }
}
