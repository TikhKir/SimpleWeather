package com.example.simpleweather.network.locationiq

import com.example.simpleweather.repository.model.LocationWithCoords

interface LocationIqApi {

    suspend fun getCoordsByCityName(cityName: String): List<LocationWithCoords>

    suspend fun getCityNameByCoords(lat: Float, lon: Float): List<LocationWithCoords>

}