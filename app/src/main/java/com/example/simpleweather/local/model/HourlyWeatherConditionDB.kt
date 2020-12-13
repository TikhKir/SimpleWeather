package com.example.simpleweather.local.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.simpleweather.repository.model.CurrentWeatherCondition
import com.example.simpleweather.repository.model.HourlyWeatherCondition

@Entity(
    tableName = "hourly_weather_conditions",
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
data class HourlyWeatherConditionDB(
    val locationParentId: Int,
    val timeStamp: Int,
    val timeZoneOffset: Int,

    val temp: Float?,
    val tempFeelsLike: Float?,
    val pressure: Int?,
    val humidity: Int?,
    val windSpeed: Float?,
    val windDeg: Int?,

    val weatherId: Int?,
    val weatherName: String?,
    val weatherDescription: String?,
    val weatherIcon: String?,

    val probabilityOfPrecipitation: Float?,
    val snowVolume: Float?,
    val rainVolume: Float?
) {
    @PrimaryKey(autoGenerate = true)
    var hourlyConditionId: Int? = null

    fun toHourlyWeatherCondition(): HourlyWeatherCondition {
        return HourlyWeatherCondition(
            timeStamp,
            timeZoneOffset,
            temp,
            tempFeelsLike,
            pressure,
            humidity,
            windSpeed,
            windDeg,
            weatherId,
            weatherName,
            weatherDescription,
            weatherIcon,
            probabilityOfPrecipitation,
            snowVolume,
            rainVolume
        )
    }

    fun toCurrentWeatherCondition(): CurrentWeatherCondition {
        return CurrentWeatherCondition(
            timeStamp,
            timeZoneOffset,
            null,
            null,
            temp,
            tempFeelsLike,
            pressure,
            humidity,
            null,
            null,
            windSpeed,
            windDeg,
            weatherId,
            weatherName,
            weatherDescription,
            weatherIcon,
            snowVolume,
            rainVolume,
            null
        )
    }
}