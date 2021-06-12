package com.example.simpleweather.network.locationiq

import com.example.simpleweather.domain.model.Location
import com.example.simpleweather.utils.datawrappers.Result

interface LocationIqApi {

    suspend fun getCoordsByCityName(cityName: String): Result<List<Location>>

    suspend fun getCityNameByCoords(lat: Float, lon: Float): Result<List<Location>>

}