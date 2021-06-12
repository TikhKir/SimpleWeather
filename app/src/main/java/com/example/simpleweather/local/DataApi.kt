package com.example.simpleweather.local

import com.example.simpleweather.domain.model.CurrentCondition
import com.example.simpleweather.domain.model.DailyCondition
import com.example.simpleweather.domain.model.HourlyCondition
import com.example.simpleweather.domain.model.Location
import com.example.simpleweather.utils.datawrappers.Result
import kotlinx.coroutines.flow.Flow

interface DataApi {

    suspend fun getDailyForecast(locationId: Long): Flow<Result<List<DailyCondition>>>

    suspend fun getHourlyForecast(locationId: Long): Flow<Result<List<HourlyCondition>>>

    suspend fun getCurrentForecast(locationId: Long): Flow<Result<CurrentCondition>>


    suspend fun saveDailyForecast(locationId: Long, listDaily: List<DailyCondition>)

    suspend fun updateDailyRefreshTime(lastUpdateTime: Long, locationId: Long)

    suspend fun updateHourlyRefreshTime(lastUpdateTime: Long, locationId: Long)

    suspend fun updateCurrentRefreshTime(lastUpdateTime: Long, locationId: Long)

    suspend fun saveHourlyForecast(locationId: Long, listHourly: List<HourlyCondition>)


    suspend fun saveNewLocation(location: Location): Long

    suspend fun getSavedLocations(): Flow<List<Location>>

    suspend fun getSavedLocationsSync(): List<Location>

    suspend fun getSavedLocationById(locationId: Long): Location

    suspend fun deleteLocation(locationId: Long): Int


    suspend fun removeGarbage()

}