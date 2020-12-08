package com.example.simpleweather.local

import com.example.simpleweather.repository.model.CurrentWeatherCondition
import com.example.simpleweather.repository.model.DailyWeatherCondition
import com.example.simpleweather.repository.model.HourlyWeatherCondition
import com.example.simpleweather.repository.model.LocationWithCoords
import com.example.simpleweather.utils.datawrappers.Result
import kotlinx.coroutines.flow.Flow

interface DataApi {

    suspend fun getDailyForecast(locationId: Long): Flow<Result<List<DailyWeatherCondition>>>

    suspend fun getHourlyForecast(locationId: Long): Flow<Result<List<HourlyWeatherCondition>>>

    suspend fun getCurrentForecast(locationId: Long): Flow<Result<CurrentWeatherCondition>>


    suspend fun saveDailyForecast(locationId: Long, listDaily: List<DailyWeatherCondition>)

    suspend fun saveHourlyForecast(locationId: Long, listHourly: List<HourlyWeatherCondition>)


    suspend fun saveNewLocation(location: LocationWithCoords): Long

    suspend fun getSavedLocations(): Flow<List<LocationWithCoords>>

    suspend fun getSavedLocationById(locationId: Long): LocationWithCoords

    suspend fun deleteLocation(locationId: Long): Int


    suspend fun removeGarbage()

}