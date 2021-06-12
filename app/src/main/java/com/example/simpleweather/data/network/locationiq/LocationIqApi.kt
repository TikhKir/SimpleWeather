package com.example.simpleweather.data.network.locationiq

import com.example.simpleweather.domain.datawrappers.Result
import com.example.simpleweather.domain.model.Location

interface LocationIqApi {

    suspend fun getCoordsByCityName(cityName: String): Result<List<Location>>

    suspend fun getCityNameByCoords(lat: Float, lon: Float): Result<List<Location>>

}