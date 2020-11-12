package com.example.simpleweather.local.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.simpleweather.repository.model.DailyWeatherCondition

@Entity(
    tableName = "daily_weather_conditions",
    indices = [Index(value = ["timeStamp"], unique = true)],
    foreignKeys = [
        ForeignKey(
            entity = LocationDB::class,
            parentColumns = ["locationId"],
            childColumns = ["locationParentId"],
            onDelete = CASCADE
        )
    ]
)
data class DailyWeatherConditionDB(

    val locationParentId: Long,
    val timeStamp: Int,

    val sunrise: Int?,
    val sunset: Int?,

    val tempDay: Float?,
    val tempEvening: Float?,
    val tempNight: Float?,
    val tempMorning: Float?,
    val tempMax: Float?,
    val tempMin: Float?,

    val tempDayFL: Float?,
    val tempEveningFL: Float?,
    val tempNightFL: Float?,
    val tempMorningFL: Float?,

    val pressure: Int?,
    val humidity: Int?,
    val dewPoint: Float?,
    val clouds: Int?,
    val windSpeed: Float?,
    val windDeg: Int?,

    val weatherId: Int?,
    val weatherName: String?,
    val weatherDescription: String?,
    val weatherIcon: String?,

    val probabilityOfPrecipitation: Float?,
    val snowVolume: Float?,
    val rainVolume: Float?,
    val uvi: Float?
) {
    @PrimaryKey(autoGenerate = true)
    var dailyConditionId: Int? = null

    fun toDailyWeatherCondition(): DailyWeatherCondition {
        return DailyWeatherCondition(
            timeStamp,
            sunrise,
            sunset,
            tempDay,
            tempEvening,
            tempNight,
            tempMorning,
            tempMax,
            tempMin,
            tempDayFL,
            tempEveningFL,
            tempNightFL,
            tempMorningFL,
            pressure,
            humidity,
            dewPoint,
            clouds,
            windSpeed,
            windDeg,
            weatherId,
            weatherName,
            weatherDescription,
            weatherIcon,
            probabilityOfPrecipitation,
            snowVolume,
            rainVolume,
            uvi
        )
    }
}