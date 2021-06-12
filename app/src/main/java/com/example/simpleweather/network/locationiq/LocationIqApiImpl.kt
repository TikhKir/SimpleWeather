package com.example.simpleweather.network.locationiq

import com.example.simpleweather.domain.model.Location
import com.example.simpleweather.utils.datawrappers.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LocationIqApiImpl @Inject constructor(
    private val locationIqService: LocationIqService
) : LocationIqApi {


    override suspend fun getCoordsByCityName(cityName: String): Result<List<Location>> {
        return wrapResponse {
            locationIqService.getCoordsListByCity(cityName)
                .map { it.toLocation() }
        }
    }

    override suspend fun getCityNameByCoords(lat: Float, lon: Float): Result<List<Location>> {
        return wrapResponse {
            val temp = locationIqService.getCityListByCoords(lat, lon)
                .toLocation()
            listOf(temp) //костыль, чтобы привести все к листу пока что
        }
    }

    private suspend fun <T> wrapResponse(coroutine: suspend () -> T): Result<T> {
        return withContext(Dispatchers.IO) {
            try {
                Result.success(coroutine.invoke())
            } catch (e: Exception) {
                Result.error(e)
            }
        }
    }

}