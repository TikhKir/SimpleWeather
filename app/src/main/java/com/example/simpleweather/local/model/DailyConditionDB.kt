package com.example.simpleweather.local.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.simpleweather.domain.model.DailyCondition
import kotlin.math.roundToInt

@Entity(
    tableName = "daily_weather_conditions",
    indices = [Index(value = ["timeStamp", "locationParentId"], unique = true)],
    foreignKeys = [
        ForeignKey(
            entity = LocationDB::class,
            parentColumns = ["locationId"],
            childColumns = ["locationParentId"],
            onDelete = CASCADE
        )
    ]
)
data class DailyConditionDB(

    val locationParentId: Long,
    val timeStamp: Int,
    val timeZoneOffset: Int,

    val sunrise: Int,
    val sunset: Int,

    val tempDay: Float,
    val tempEvening: Float,
    val tempNight: Float,
    val tempMorning: Float,
    val tempMax: Float,
    val tempMin: Float,

    val tempDayFL: Float,
    val tempEveningFL: Float,
    val tempNightFL: Float,
    val tempMorningFL: Float,

    val pressure: Int,
    val humidity: Int,
    val dewPoint: Float,
    val clouds: Int,
    val windSpeed: Float,
    val windDeg: Int,

    val weatherId: Int,
    val weatherName: String,
    val weatherDescription: String,
    val weatherIcon: String,

    val probabilityOfPrecipitation: Float,
    val snowVolume: Float,
    val rainVolume: Float,
    val uvi: Float
) {
    @PrimaryKey(autoGenerate = true)
    var dailyConditionId: Int? = null

    fun toDailyCondition(): DailyCondition {
        return DailyCondition(
            timeStamp = timeStamp,
            timeZoneOffset = timeZoneOffset,
            sunrise = sunrise,
            sunset = sunset,
            tempDay = tempDay.roundToInt(),
            tempEvening = tempEvening.roundToInt(),
            tempNight = tempNight.roundToInt(),
            tempMorning = tempMorning.roundToInt(),
            tempMax = tempMax.roundToInt(),
            tempMin = tempMin.roundToInt(),
            tempDayFL = tempDayFL.roundToInt(),
            tempEveningFL = tempEveningFL.roundToInt(),
            tempNightFL = tempNightFL.roundToInt(),
            tempMorningFL = tempMorningFL.roundToInt(),
            pressure = pressure,
            humidity = humidity,
            dewPoint = dewPoint,
            clouds = clouds,
            windSpeed = windSpeed,
            windDeg = windDeg,
            weatherId = weatherId,
            weatherName = weatherName,
            weatherIcon = weatherIcon,
            snowVolume = snowVolume,
            rainVolume = rainVolume,
            probabilityOfPrecipitation = probabilityOfPrecipitation,
            weatherDescription = weatherDescription,
            uvi = uvi
        )
    }
}