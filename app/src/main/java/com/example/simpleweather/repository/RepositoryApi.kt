package com.example.simpleweather.repository

import com.example.simpleweather.repository.model.CurrentWeatherCondition
import com.example.simpleweather.repository.model.DailyWeatherCondition
import com.example.simpleweather.repository.model.HourlyWeatherCondition
import com.example.simpleweather.repository.model.LocationWithCoords
import com.example.simpleweather.utils.datawrappers.Result
import kotlinx.coroutines.flow.Flow

interface RepositoryApi {

    suspend fun getCoordByCityName(cityName: String): List<LocationWithCoords>

    suspend fun getCityNameByCoords(lat: Float, lon: Float): List<LocationWithCoords>


    suspend fun getDailyCondition(lat: Float, lon: Float): List<DailyWeatherCondition>

    suspend fun getHourlyCondition(lat: Float, lon: Float): Result<List<HourlyWeatherCondition>>
    suspend fun getHourlyCondition(locationId: Long): Flow<Result<List<HourlyWeatherCondition>>>

    suspend fun getCurrentCondition(lat: Float, lon: Float): CurrentWeatherCondition


    suspend fun getSavedLocations(): Flow<List<LocationWithCoords>>

    suspend fun saveNewLocation(location: LocationWithCoords): Long

    suspend fun deleteLocation(locationId: Long): Int


    suspend fun saveDailyForecast(locationId: Long, listDaily: List<DailyWeatherCondition>)

    suspend fun saveHourlyForecast(locationId: Long, listHourly: List<HourlyWeatherCondition>)


}