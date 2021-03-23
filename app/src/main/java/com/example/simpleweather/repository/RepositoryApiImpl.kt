package com.example.simpleweather.repository

import android.util.Log
import com.example.simpleweather.local.DataApi
import com.example.simpleweather.network.locationiq.LocationIqApi
import com.example.simpleweather.network.openweather.OpenWeatherApi
import com.example.simpleweather.repository.model.CurrentWeatherCondition
import com.example.simpleweather.repository.model.DailyWeatherCondition
import com.example.simpleweather.repository.model.HourlyWeatherCondition
import com.example.simpleweather.repository.model.LocationWithCoords
import com.example.simpleweather.utils.MINIMAL_REFRESH_INTERVAL
import com.example.simpleweather.utils.MINIMAL_REFRESH_INTERVAL_CURRENT
import com.example.simpleweather.utils.datawrappers.Result
import com.example.simpleweather.utils.datawrappers.ResultType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.threeten.bp.Instant
import javax.inject.Inject

class RepositoryApiImpl @Inject constructor(
    private val openWeatherApi: OpenWeatherApi,
    private val locationIqApi: LocationIqApi,
    private val dataApi: DataApi
) : RepositoryApi {

    override suspend fun getDailyConditionWithoutCaching(
        lat: Float,
        lon: Float
    ): Flow<Result<List<DailyWeatherCondition>>> {
        return flowOf(openWeatherApi.getDailyCondition(lat, lon))
    }

    override suspend fun getDailyCondition(locationId: Long): Flow<Result<List<DailyWeatherCondition>>> {
        val location = dataApi.getSavedLocationById(locationId)
        val currentTime = Instant.now().epochSecond
        val timeDifference = currentTime - location.refreshTimeDaily

        return if (timeDifference > MINIMAL_REFRESH_INTERVAL) {
            val netResponse =
                openWeatherApi.getDailyCondition(location.latitude, location.longitude)
            if (netResponse.resultType == ResultType.SUCCESS) {
                netResponse.data?.let {
                    saveDailyForecast(locationId, it)
                    dataApi.updateDailyRefreshTime(currentTime, locationId)
                }
                flowOf(netResponse)
            } else {
                Log.e("NET_DAILY_UPDATE_FAIL", netResponse.error?.message.toString())
                dataApi.getDailyForecast(locationId)
            }
        } else {
            Log.e("NET_DAILY_UPDATE_FAIL", "Refresh interval is not over")
            dataApi.getDailyForecast(locationId)
        }
    }

    override suspend fun getHourlyConditionWithoutCaching(
        lat: Float,
        lon: Float
    ): Flow<Result<List<HourlyWeatherCondition>>> {
        return flowOf(openWeatherApi.getHourlyCondition(lat, lon))
    }

    override suspend fun getHourlyCondition(locationId: Long): Flow<Result<List<HourlyWeatherCondition>>> {
        val location = dataApi.getSavedLocationById(locationId)
        val currentTime = Instant.now().epochSecond
        val timeDifference = currentTime - location.refreshTimeHourly

        return if (timeDifference > MINIMAL_REFRESH_INTERVAL) {
            val netResponse =
                openWeatherApi.getHourlyCondition(location.latitude, location.longitude)
            if (netResponse.resultType == ResultType.SUCCESS) {
                netResponse.data?.let {
                    saveHourlyForecast(locationId, it)
                    dataApi.updateHourlyRefreshTime(currentTime, locationId)
                }
                flowOf(netResponse)
            } else {
                Log.e("NET_HOURLY_UPDATE_FAIL", netResponse.error?.message.toString())
                dataApi.getHourlyForecast(locationId)
            }
        } else {
            Log.e("NET_HOURLY_UPDATE_FAIL", "Refresh interval is not over")
            dataApi.getHourlyForecast(locationId)
        }
    }

    override suspend fun getCurrentConditionWithoutCaching(
        lat: Float,
        lon: Float
    ): Flow<Result<CurrentWeatherCondition>> {
        return flowOf(openWeatherApi.getCurrentCondition(lat, lon))
    }

    override suspend fun getCurrentCondition(locationId: Long): Flow<Result<CurrentWeatherCondition>> {
        val location = dataApi.getSavedLocationById(locationId)
        val currentTime = Instant.now().epochSecond
        val timeDifference = currentTime - location.refreshTimeHourly

        return if (timeDifference > MINIMAL_REFRESH_INTERVAL_CURRENT) {
            val netResponse = openWeatherApi.getCurrentCondition(location.latitude, location.longitude)
            if (netResponse.resultType == ResultType.SUCCESS) {
                dataApi.updateCurrentRefreshTime(currentTime, locationId)
                flowOf(netResponse)
            } else {
                Log.e("NET_CURRENT_UPDATE_FAIL", netResponse.error?.message.toString())
                dataApi.getCurrentForecast(locationId)
            }
        } else {
            Log.e("NET_CURRENT_UPDATE_FAIL", "Refresh interval is not over")
            dataApi.getCurrentForecast(locationId)
        }
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


    override suspend fun saveDailyForecast(
        locationId: Long,
        listDaily: List<DailyWeatherCondition>
    ) {
        dataApi.saveDailyForecast(locationId, listDaily)
    }

    override suspend fun saveHourlyForecast(
        locationId: Long,
        listHourly: List<HourlyWeatherCondition>
    ) {
        dataApi.saveHourlyForecast(locationId, listHourly)
    }

    override suspend fun getCoordByCityName(cityName: String): Result<List<LocationWithCoords>> {
        return locationIqApi.getCoordsByCityName(cityName)
    }

    override suspend fun getCityNameByCoords(
        lat: Float,
        lon: Float
    ): Result<List<LocationWithCoords>> {
        return locationIqApi.getCityNameByCoords(lat, lon)
    }


}