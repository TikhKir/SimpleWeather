package com.example.simpleweather.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.simpleweather.local.model.DailyWeatherConditionDB
import com.example.simpleweather.local.model.HourlyWeatherConditionDB
import com.example.simpleweather.local.model.LocationDB

@Database(
    entities = [
        LocationDB::class,
        HourlyWeatherConditionDB::class,
        DailyWeatherConditionDB::class
    ],
    version = 6,
    exportSchema = false
)
abstract class WeatherDataBase: RoomDatabase() {

    companion object {
        const val DB_NAME = "simpleWeather.db"
    }

    abstract fun weatherDao(): WeatherDao
}