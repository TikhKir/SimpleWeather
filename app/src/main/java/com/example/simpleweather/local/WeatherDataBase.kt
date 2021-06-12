package com.example.simpleweather.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.simpleweather.local.model.DailyConditionDB
import com.example.simpleweather.local.model.HourlyConditionDB
import com.example.simpleweather.local.model.LocationDB

@Database(
    entities = [
        LocationDB::class,
        HourlyConditionDB::class,
        DailyConditionDB::class
    ],
    version = 10,
    exportSchema = false
)
abstract class WeatherDataBase: RoomDatabase() {

    companion object {
        const val DB_NAME = "simpleWeather.db"
    }

    abstract fun weatherDao(): WeatherDao
}