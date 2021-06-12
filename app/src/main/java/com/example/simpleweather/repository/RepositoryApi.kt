package com.example.simpleweather.repository

import com.example.simpleweather.domain.datawrappers.Result
import com.example.simpleweather.domain.model.CurrentCondition
import com.example.simpleweather.domain.model.DailyCondition
import com.example.simpleweather.domain.model.HourlyCondition
import com.example.simpleweather.domain.model.Location
import kotlinx.coroutines.flow.Flow

interface RepositoryApi {

    suspend fun getCoordByCityName(cityName: String): Result<List<Location>>

    suspend fun getCityNameByCoords(lat: Float, lon: Float): Result<List<Location>>


    suspend fun getDailyConditionWithoutCaching(lat: Float, lon: Float): Flow<Result<List<DailyCondition>>>
    suspend fun getDailyCondition(locationId: Long): Flow<Result<List<DailyCondition>>>

    suspend fun getHourlyConditionWithoutCaching(lat: Float, lon: Float): Flow<Result<List<HourlyCondition>>>
    suspend fun getHourlyCondition(locationId: Long): Flow<Result<List<HourlyCondition>>>

    suspend fun getCurrentConditionWithoutCaching(lat: Float, lon: Float): Flow<Result<CurrentCondition>>
    suspend fun getCurrentCondition(locationId: Long): Flow<Result<CurrentCondition>>


    suspend fun getSavedLocations(): Flow<List<Location>>

    suspend fun saveNewLocation(location: Location): Long

    suspend fun deleteLocation(locationId: Long): Int


    suspend fun saveDailyForecast(locationId: Long, listDaily: List<DailyCondition>)

    suspend fun saveHourlyForecast(locationId: Long, listHourly: List<HourlyCondition>)


}