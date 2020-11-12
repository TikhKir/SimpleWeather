package com.example.simpleweather.local

import com.example.simpleweather.repository.model.DailyWeatherCondition
import com.example.simpleweather.repository.model.HourlyWeatherCondition
import com.example.simpleweather.repository.model.LocationWithCoords
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.flow.Flow

interface DataApi {

    suspend fun getDailyForecast(): Deferred<List<HourlyWeatherCondition>>

    suspend fun getHourlyForecast(): Deferred<List<HourlyWeatherCondition>>

    suspend fun saveDailyForecast(locationId: Long, listDaily: List<DailyWeatherCondition>)

    suspend fun saveHourlyForecast(listHourly: List<HourlyWeatherCondition>)

    suspend fun saveNewLocation(location: LocationWithCoords): Long

    suspend fun getSavedLocations(): Flow<List<LocationWithCoords>>

    suspend fun deleteLocation(locationId: Long): Int

    suspend fun removeGarbage()

}