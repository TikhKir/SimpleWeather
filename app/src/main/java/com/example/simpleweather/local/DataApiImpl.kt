package com.example.simpleweather.local

import com.example.simpleweather.repository.model.DailyWeatherCondition
import com.example.simpleweather.repository.model.HourlyWeatherCondition
import com.example.simpleweather.repository.model.LocationWithCoords
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DataApiImpl @Inject constructor(
    private val weatherDao: WeatherDao
): DataApi {

    override suspend fun getDailyForecast(): Deferred<List<HourlyWeatherCondition>> {
        TODO("получать только свежие данные не раньше нужного таймстампа")
    }

    override suspend fun getHourlyForecast(): Deferred<List<HourlyWeatherCondition>> {
        TODO("получать только свежие данные не раньше нужного таймстампа")
    }

    override suspend fun saveDailyForecast(locationId: Long, listDaily: List<DailyWeatherCondition>) {
        val transformedList = listDaily
            .map { it.toDailyWeatherConditionDB(locationId) }
        weatherDao.saveDailyForecast(transformedList)
        }




    override suspend fun saveHourlyForecast(listHourly: List<HourlyWeatherCondition>) {
        TODO("Not yet implemented")
    }




    override suspend fun saveNewLocation(location: LocationWithCoords): Long {
        return weatherDao.saveNewLocation(location.toLocationDB())
    }

    override suspend fun getSavedLocations(): Flow<List<LocationWithCoords>> {
        return weatherDao.getSavedLocations()
            .map {
            it.map {
                it.toLocationWithCoords()
            }
        }
    }

    override suspend fun deleteLocation(locationId: Long): Int {
        return weatherDao.deleteLocation(locationId)
    }




    override suspend fun removeGarbage() {
        TODO("здесь тоже нужно отправлять таймстамп")
    }
}