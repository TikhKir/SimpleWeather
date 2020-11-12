package com.example.simpleweather.network.locationiq

import com.example.simpleweather.repository.model.LocationWithCoords
import javax.inject.Inject

class LocationIqApiImpl @Inject constructor(
    private val locationIqService: LocationIqService
) : LocationIqApi {


    override suspend fun getCoordsByCityName(cityName: String): List<LocationWithCoords> {
        return locationIqService.getCoordsListByCity(cityName)
            .map { it.toLocationWithCoords() }
    }

    override suspend fun getCityNameByCoords(lat: Float, lon: Float
    ): List<LocationWithCoords> {
        val temp = locationIqService.getCityListByCoords(lat, lon)
            .toLocationWithCoords()
        return listOf(temp) //костыль, чтобы привести все к листу пока что
    }

}