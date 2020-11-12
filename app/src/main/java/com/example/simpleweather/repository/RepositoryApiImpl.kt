package com.example.simpleweather.repository

import com.example.simpleweather.local.DataApi
import com.example.simpleweather.network.locationiq.LocationIqApi
import com.example.simpleweather.network.openweather.OpenWeatherApi
import com.example.simpleweather.repository.model.CurrentWeatherCondition
import com.example.simpleweather.repository.model.DailyWeatherCondition
import com.example.simpleweather.repository.model.HourlyWeatherCondition
import com.example.simpleweather.repository.model.LocationWithCoords
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RepositoryApiImpl @Inject constructor(
    private val openWeatherApi: OpenWeatherApi,
    private val locationIqApi: LocationIqApi,
    private val dataApi: DataApi
): RepositoryApi {


    override suspend fun getCoordByCityName(cityName: String): List<LocationWithCoords> {
        return locationIqApi.getCoordsByCityName(cityName)
    }

    override suspend fun getCityNameByCoords(lat: Float, lon: Float): List<LocationWithCoords> {
        return locationIqApi.getCityNameByCoords(lat, lon)
    }



    override suspend fun getDailyCondition(lat: Float, lon: Float): List<DailyWeatherCondition> {
        return openWeatherApi.getAllForecastByCoord(lat, lon)
    }

    override suspend fun getHourlyCondition(lat: Float, lon: Float): List<HourlyWeatherCondition> {
        return openWeatherApi.getHourlyCondition(lat, lon)
    }

    override suspend fun getCurrentCondition(lat: Float, lon: Float): CurrentWeatherCondition {
        return openWeatherApi.getCurrentCondition(lat, lon)
    }




    override suspend fun getSavedLocations(): Flow<List<LocationWithCoords>> {
        return dataApi.getSavedLocations()
    }

    override suspend fun saveNewLocation(location: LocationWithCoords): Long {
        return dataApi.saveNewLocation(location)
    }

    override suspend fun deleteLocation(locationId: Long): Int {
        return dataApi.deleteLocation(locationId)
    }



    override suspend fun saveDailyForecast(locationId: Long, listDaily: List<DailyWeatherCondition>) {
        dataApi.saveDailyForecast(locationId, listDaily)
    }


}