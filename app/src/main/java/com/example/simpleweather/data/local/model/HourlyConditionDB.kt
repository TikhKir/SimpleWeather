package com.example.simpleweather.data.local.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.simpleweather.domain.model.CurrentCondition
import com.example.simpleweather.domain.model.HourlyCondition
import kotlin.math.roundToInt

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
data class HourlyConditionDB(
    val locationParentId: Int,
    val timeStamp: Int,
    val timeZoneOffset: Int,

    val temp: Float,
    val tempFeelsLike: Float,
    val pressure: Int,
    val humidity: Int,
    val windSpeed: Float,
    val windDeg: Int,

    val weatherId: Int,
    val weatherName: String,
    val weatherDescription: String,
    val weatherIcon: String,

    val probabilityOfPrecipitation: Float,
    val snowVolume: Float,
    val rainVolume: Float
) {
    @PrimaryKey(autoGenerate = true)
    var hourlyConditionId: Int? = null

    fun toHourlyCondition(): HourlyCondition {
        return HourlyCondition(
            timeStamp = timeStamp,
            timeZoneOffset = timeZoneOffset,
            temp = temp.roundToInt(),
            tempFL = tempFeelsLike.roundToInt(),
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

    fun toCurrentCondition(): CurrentCondition {
        return CurrentCondition(
            timeStamp,
            timeZoneOffset,
            0,
            0,
            temp = temp.roundToInt(),
            tempFL = tempFeelsLike.roundToInt(),
            pressure = pressure,
            humidity = humidity,
            dewPoint = 0F,
            clouds = 0,
            windSpeed = windSpeed,
            windDeg = windDeg,
            weatherId = weatherId,
            weatherName = weatherName,
            weatherDescription = weatherDescription,
            weatherIcon = weatherIcon,
            snowVolumeLastHour = snowVolume,
            rainVolumeLastHour = rainVolume,
            allVolumeLastHour = (snowVolume + rainVolume),
            uvi = 0F
        )
    }

}