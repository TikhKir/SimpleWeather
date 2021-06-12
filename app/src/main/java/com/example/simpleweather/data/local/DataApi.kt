package com.example.simpleweather.data.local

import com.example.simpleweather.domain.datawrappers.Result
import com.example.simpleweather.domain.model.CurrentCondition
import com.example.simpleweather.domain.model.DailyCondition
import com.example.simpleweather.domain.model.HourlyCondition
import com.example.simpleweather.domain.model.Location
import kotlinx.coroutines.flow.Flow

interface DataApi {

    suspend fun getDailyForecast(locationId: Long): Flow<Result<List<DailyCondition>>>

    suspend fun getHourlyForecast(locationId: Long): Flow<Result<List<HourlyCondition>>>

    suspend fun getCurrentForecast(locationId: Long): Flow<Result<CurrentCondition>>


    suspend fun saveDailyForecast(locationId: Long, listDaily: List<DailyCondition>)

    suspend fun updateDailyRefreshTime(locationId: Long)

    suspend fun updateHourlyRefreshTime(locationId: Long)

    suspend fun updateCurrentRefreshTime(locationId: Long)

    suspend fun saveHourlyForecast(locationId: Long, listHourly: List<HourlyCondition>)


    suspend fun saveNewLocation(location: Location): Long

    suspend fun getSavedLocations(): Flow<List<Location>>

    suspend fun getSavedLocationsSync(): List<Location>

    suspend fun getSavedLocationById(locationId: Long): Location

    suspend fun deleteLocation(locationId: Long): Int


    suspend fun removeGarbage()

}