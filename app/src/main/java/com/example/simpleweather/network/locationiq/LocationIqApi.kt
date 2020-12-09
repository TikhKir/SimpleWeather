package com.example.simpleweather.network.locationiq

import com.example.simpleweather.repository.model.LocationWithCoords
import com.example.simpleweather.utils.datawrappers.Result

interface LocationIqApi {

    suspend fun getCoordsByCityName(cityName: String): Result<List<LocationWithCoords>>

    suspend fun getCityNameByCoords(lat: Float, lon: Float): Result<List<LocationWithCoords>>

}