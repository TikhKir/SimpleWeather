package com.example.simpleweather.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.simpleweather.local.model.DailyWeatherConditionDB
import com.example.simpleweather.local.model.LocationDB
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {

    @Insert(entity = LocationDB::class, onConflict = OnConflictStrategy.IGNORE)
    suspend fun saveNewLocation(location: LocationDB): Long

    @Query("SELECT * FROM locations")
    fun getSavedLocations(): Flow<List<LocationDB>>

    @Query("DELETE FROM locations WHERE locationId = :locationId")
    suspend fun deleteLocation(locationId: Long): Int



    @Insert(entity = DailyWeatherConditionDB::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveDailyForecast(dailyForecastList: List<DailyWeatherConditionDB>)


}